package com.example.playlistmaker.player.ui

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.TRACK_DATA
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerStateStatus
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private val playerViewModel: PlayerViewModel by viewModel { parametersOf(track.previewUrl)  }
    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private var stateFavoriteButton = false
    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        track = intent.getParcelableExtra(TRACK_DATA) ?:
        throw IllegalArgumentException("Track data not found in Intent")

        initializeUIComponents()
        observeViewModel()

        binding.favoriteButton.setOnClickListener{
            lifecycleScope.launch {
                playerViewModel.onFavoriteClicked(track)
            }
        }

        playerViewModel.getFavoriteLiveData().observe(this) { state ->
            stateFavoriteButton = state
            updatefavoriteButton (stateFavoriteButton)
        }
    }

    private fun updatefavoriteButton (stateFavoriteButton: Boolean) {
        if (stateFavoriteButton) {
            binding.favoriteButton.setImageResource(R.drawable.ic_lliked)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
        }
    }

    override fun onPause() {
        super.onPause()
            playerViewModel.pausePlayer()
    }

    private fun initializeUIComponents() {

        val trackReleaseYear = track.releaseDate?.substring(0..3)

        binding.playButton.setOnClickListener {
            playbackControl()
        }

        track.let {
            binding.trackNamePlayer.text = track.trackName
            binding.artistNamePlayer.text = track.artistName
            binding.trackTimeMillisModelTrack.text = dateFormat.format(track.trackTimeMillis)
            binding.trackOllectionName.text = track.collectionName ?: ""
            binding.trackReleaseDate.text = trackReleaseYear
            binding.trackGenreName.text = track.primaryGenreName ?: ""
            binding.trackCountry.text = track.country ?: ""
        }

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.playerAlbum)

        binding.collection.isVisible = binding.trackOllectionName.text.isNotEmpty()
        binding.trackOllectionName.isVisible = binding.trackOllectionName.text.isNotEmpty()

        binding.playlistBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        playerViewModel.playerState.observe(this) { state ->
            updateUI(state)
        }
        playerViewModel.currentPosition.observe(this) { position ->
            binding.playbackProgress.text = dateFormat.format(position)
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
        when (playerViewModel.playerState.value) {
            is PlayerStateStatus.STATE_PLAYING -> playerViewModel.pausePlayer()
            is PlayerStateStatus.STATE_PREPARED, is PlayerStateStatus.STATE_PAUSED -> playerViewModel.startPlayer()
            else -> {}
        }
    }
}