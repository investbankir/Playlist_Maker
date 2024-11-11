    package com.example.playlistmaker.player.ui

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.core.os.bundleOf
    import androidx.core.view.isVisible
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.lifecycleScope
    import androidx.navigation.fragment.findNavController
    import com.bumptech.glide.Glide
    import com.bumptech.glide.load.resource.bitmap.RoundedCorners
    import com.example.playlistmaker.R
    import com.example.playlistmaker.databinding.FragmentPlayerBinding
    import com.example.playlistmaker.player.domain.models.PlayerStateStatus
    import com.example.playlistmaker.search.domain.models.Track
    import com.google.android.material.bottomsheet.BottomSheetBehavior
    import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.launch
    import java.text.SimpleDateFormat
    import java.util.Locale
    import org.koin.androidx.viewmodel.ext.android.viewModel
    import org.koin.core.parameter.parametersOf

    class PlayerFragment : Fragment() {
        companion object{
            private const val ARGS_TRACK_ID = "track_id"
            fun createArgs(track: Track): Bundle =
                bundleOf(ARGS_TRACK_ID to track)
            private const val CLICK_DEBOUNCE_DELAY = 1000L
        }
        private val viewModel: PlayerViewModel by viewModel { parametersOf(track?.previewUrl)  }
        private var track: Track? = null
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        private val playlists = arrayListOf<Playlist>()
        private var isClickAllowed = true

        private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
        private lateinit var adapter: BottomSheetAdapter

        private var _binding: FragmentPlayerBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentPlayerBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            track = arguments?.getParcelable(ARGS_TRACK_ID) ?:
                    throw IllegalArgumentException("Track data not found in Intent")

            adapter = BottomSheetAdapter { playlists ->
                if (clickDebounce()) {
                    viewModel.updatePlaylist(playlists, track!!)
                }
            }

            adapter.playlists = playlists

            binding.rvBottomSheet.adapter = adapter


            val bottomSheetContainer = binding.playlistsBottomSheet
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer as View).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

            initializeUIComponents()
            observeViewModel()

            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        } else -> {
                        binding.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val alpha = (slideOffset +1) / 2
                    binding.overlay.alpha = alpha
                }

            })

            binding.addTrackPlaylist.setOnClickListener{
                lifecycleScope.launch {
                    viewModel.getPlaylists()
                    binding.playlistsBottomSheet.isVisible = true
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            lifecycleScope.launch {
                val isFavorite = viewModel.isTrackFavorite(track!!.trackId)
                updatefavoriteButton(isFavorite)
            }

            binding.playButton.setOnClickListener {
                playbackControl()
            }

            binding.favoriteButton.setOnClickListener{
                lifecycleScope.launch {
                    track?.let { viewModel.onFavoriteClicked(it) }
                }
            }

            binding.newPlayListButtonBottomSheet.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_createNewPlaylistFragment)
            }
        }
        override fun onPause() {
            super.onPause()
                viewModel.pausePlayer()
        }

        private fun initializeUIComponents() {
            val trackReleaseYear = track?.releaseDate?.substring(0..3)

            track?.let {
                binding.trackNamePlayer.text = it.trackName
                binding.artistNamePlayer.text = it.artistName
                binding.trackTimeMillisModelTrack.text = dateFormat.format(it.trackTimeMillis)
                binding.trackOllectionName.text = it.collectionName ?: ""
                binding.trackReleaseDate.text = trackReleaseYear
                binding.trackGenreName.text = it.primaryGenreName ?: ""
                binding.trackCountry.text = it.country ?: ""
            }

            Glide.with(this)
                .load(track?.getCoverArtwork())
                .placeholder(R.drawable.ic_player_placeholder)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(binding.playerAlbum)

            binding.collection.isVisible = binding.trackOllectionName.text.isNotEmpty()
            binding.trackOllectionName.isVisible = binding.trackOllectionName.text.isNotEmpty()

            binding.playlistBackButton.setOnClickListener {
                findNavController().popBackStack()        }
        }
        private fun observeViewModel() {
            viewModel.playerState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
            }

            viewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            binding.playbackProgress.text = dateFormat.format(position)
            }

            viewModel.getFavoriteLiveData().observe(viewLifecycleOwner) { isFavorite ->
            updatefavoriteButton(isFavorite)
            }

            viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
                adapter.updatePlaylists(playlists)
            }

            viewModel.addTrackResult.observe(viewLifecycleOwner) { (isAdded, playlistName) ->
                if (isAdded) {
                    Toast.makeText(requireContext(), "Добавлено в плейлист \"${playlistName}\"", Toast.LENGTH_SHORT).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    Toast.makeText(requireContext(), "Трек уже добавлен в плейлист \"$playlistName\"", Toast.LENGTH_SHORT).show()
                }
            }
        }
        private fun updateUI(state: PlayerStateStatus) {
            when (state) {
                is PlayerStateStatus.STATE_PLAYING -> binding.playButton.setImageResource(R.drawable.ic_pausebutton)
                is PlayerStateStatus.STATE_PAUSED, is PlayerStateStatus.STATE_PREPARED -> binding.playButton.setImageResource(R.drawable.ic_playbutton)
                else -> {}
            }
        }

        private fun playbackControl() {
            when (viewModel.playerState.value) {
                is PlayerStateStatus.STATE_PLAYING -> viewModel.pausePlayer()
                is PlayerStateStatus.STATE_PREPARED, is PlayerStateStatus.STATE_PAUSED -> viewModel.startPlayer()
                else -> {}
            }
        }
        private fun updatefavoriteButton (isFavorite: Boolean) {
            if (isFavorite) {
                binding.favoriteButton.setImageResource(R.drawable.ic_lliked)
            } else {
                binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
            }
        }
        private fun clickDebounce() : Boolean{
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(CLICK_DEBOUNCE_DELAY)
                    isClickAllowed = true
                }
            }
            return current
        }

    }