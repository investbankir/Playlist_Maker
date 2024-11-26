    package com.example.playlistmaker.selected_playlist.ui

    import android.content.Context
    import android.content.Intent
    import android.content.res.Resources
    import android.graphics.Rect
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.view.WindowManager
    import android.widget.Toast
    import androidx.core.os.bundleOf
    import androidx.core.view.isVisible
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.lifecycleScope
    import org.koin.androidx.viewmodel.ext.android.viewModel
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.bumptech.glide.Glide
    import com.bumptech.glide.load.resource.bitmap.RoundedCorners
    import com.example.playlistmaker.R
    import com.example.playlistmaker.databinding.FragmentSelectedPlaylistBinding
    import com.example.playlistmaker.search.domain.models.Track
    import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
    import com.example.playlistmaker.player.ui.PlayerFragment
    import com.example.playlistmaker.createNewPlaylist.ui.PlaylistEditorFragment
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import com.google.android.material.bottomsheet.BottomSheetBehavior
    import com.google.android.material.dialog.MaterialAlertDialogBuilder
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.launch
    import java.text.SimpleDateFormat
    import java.util.Locale
    import kotlin.time.Duration.Companion.milliseconds


    class SelectedPlaylistFragment: Fragment() {

        companion object{
            private const val ARGS_PLAYLIST_ID = "playlist_id"
            fun createArgs(playlistId: Long): Bundle =
                bundleOf(ARGS_PLAYLIST_ID to playlistId)
            private const val CLICK_DEBOUNCE_DELAY = 1000L
        }

        private val viewModel by viewModel<SelectedPlaylistViewModel>()
        private lateinit var binding: FragmentSelectedPlaylistBinding

        lateinit var confirmDialog: MaterialAlertDialogBuilder //интегрирую возможность использования диалога при удалении трека
        lateinit var adapter: SelectedPlaylistAdapter
        private var isClickAllowed = true
        private var tracks = ArrayList<Track>()
        private var playlist: Playlist? = null
        //private var playlistId: Long = 0L
        private val playlistId: Long by lazy { requireArguments().getLong(ARGS_PLAYLIST_ID) }
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentSelectedPlaylistBinding.inflate(inflater,container,false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.playlistBackButton.setOnClickListener{
                findNavController().popBackStack()
            }

            viewModel.getPlaylistById(playlistId)
            viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) {
                playlist = it
                if (playlist != null) showPlaylist(playlist!!)
            }

            viewModel.getTracksFromPlaylistsLiveData().observe(viewLifecycleOwner) { tracklist ->
                binding.durationAllTracks.text ="${playlist?.totalPlaylistTime?.milliseconds?.inWholeMinutes} ${playlist?.endingMinute}"
                binding.numberOfTracks.text = viewModel.rightEndingTracks(tracklist.size)
                binding.quantityTracks.text = viewModel.rightEndingTracks(tracklist.size)
                showTracks(tracklist)
            }


            val bottomSheetConteiner = binding.tracksBottomSheet
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetConteiner).apply {
                state = BottomSheetBehavior.STATE_HIDDEN//Разобраться со статусами БШ
            }

            bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
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
                    binding.overlay.alpha = slideOffset
                }
            })

            val editingBottomSheetContainer = binding.editingBottomSheet
            val editingBottomSheetBehavior = BottomSheetBehavior.from(editingBottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

            editingBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.overlay.isVisible = true
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        } else -> {
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset
                }
            })

            adapter = SelectedPlaylistAdapter( { track ->
                if (clickDebounce()) {
                    findNavController().navigate(R.id.action_selectedPlaylistFragment_to_playerFragment,
                        PlayerFragment.createArgs(track))
                }
            }, { track ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.deleteTrack)
                    .setMessage(R.string.acceptDeleteTrack)
                    .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(getString(R.string.delete)) { _, _ ->
                        playlist?.let {
                            tracks.remove(track)
                            viewModel.updatePlaylist(it, track)
                        }
                        viewModel.deleteTrackById(track.trackId)
                        playlist?.tracksList?.let { viewModel.getTracksFromPlaylists(it) }
                    }.show()
                true
            }
            )

            adapter.tracks = tracks
            binding.rvTrackList.adapter = adapter
            binding.rvTrackList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
            showTracks(tracks) // Не даёт результатов отображения в РВ. Перепроверить

            binding.share.setOnClickListener {
            sharePlaylist()
            }

            binding.menu.setOnClickListener{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                editingBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            binding.sharePlaylist.setOnClickListener{
                sharePlaylist()
                editingBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            binding.editPlaylist.setOnClickListener{
                findNavController().navigate(
                    R.id.action_selectedPlaylistFragment_to_playlistEditorFragment,
                    PlaylistEditorFragment.createArgs(playlistId!!))
            }

            binding.deletePlaylist.setOnClickListener {
                editingBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                deletePlaylist()
            }
        }
        private fun sharePlaylist() {
            if (tracks.isEmpty()) {
                Toast.makeText(requireContext(), R.string.shareMessage, Toast.LENGTH_SHORT ).show()
            } else {
                var shareMessage = playlist?.playlistName + "\n"+
                        (playlist?.playlistDescription?: "") + "\n"+
                        viewModel.rightEndingTracks(tracks.size) + "\n"
                for (index in tracks.indices) {
                    val track = tracks[index]
                    val converterTrackTime = dateFormat.format(track.trackTimeMillis)
                    val str = "\n" + (index + 1).toString() + ". " + track.artistName + " '" + track.trackName + "' " + converterTrackTime
                    shareMessage += str
                }
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.titleSharePlaylist))
                startActivity(chooser)
            }
        }

        private fun deletePlaylist() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.deletePlaylist))
                .setMessage(getString(R.string.deletePlaylistMessege) + " \"" + playlist?.playlistName + "\"?")
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deletePlaylistById(playlistId)
                    findNavController().popBackStack()
                }.show()
        }

        fun showPlaylist(playlist: Playlist) {
            Glide.with(this)
                .load(playlist.artworkUri)
                .placeholder(R.drawable.ic_player_placeholder)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(binding.playerCover)
            binding.playlistName.text = playlist.playlistName
            binding.playlistDescription.text = playlist.playlistDescription

            Glide.with(this)
                .load(playlist.artworkUri)
                .placeholder(R.drawable.ic_album_placeholder)
                .centerCrop()
                .into(binding.album)
            binding.plName.text = playlist.playlistName

        }
        private fun showTracks (tracklist: List<Track>) {
            if (tracklist.isEmpty()) {
                binding.rvTrackList.isVisible = false
                binding.noTracksMassage.isVisible = true
            } else {
                binding.rvTrackList.isVisible = true
                binding.noTracksMassage.isVisible = false
                tracks.clear()
                tracks.addAll(tracklist)
                adapter.notifyDataSetChanged()
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

//        fun Fragment.getBottomNavHeight(): Int {
//            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//            return bottomNav?.height ?: 0
//        }
//
//        fun View.getDistanceToBottomScreen(fragment: Fragment, marginTop: Int): Int {
//            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//
//            val rect = Rect()
//            this.getGlobalVisibleRect(rect)
//
//            val windowMetrics = Resources.getSystem().displayMetrics
//            val windowHeight = windowMetrics.heightPixels
//
//            val navigationHeight = fragment.getBottomNavHeight()
//
//            return windowHeight - rect.bottom - navigationHeight - marginTop
//        }
//
//        binding.buttonShare.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//            val mainBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
//            mainBottomSheetBehavior.peekHeight = v.getDistanceToBottomScreen(this, 25)
//        }
    }