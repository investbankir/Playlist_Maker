package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.TRACK_DATA
import com.example.playlistmaker.player.domain.models.PlayerStateStatus
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private val playerViewModel: PlayerViewModel by viewModel { parametersOf(track.previewUrl)  }
    private lateinit var track: Track
    private lateinit var playbackProgress: TextView
    private lateinit var playButton: ImageButton
    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        track = intent.getParcelableExtra(TRACK_DATA) ?:
        throw IllegalArgumentException("Track data not found in Intent")

        initializeUIComponents()
        observeViewModel()
}

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    private fun initializeUIComponents() {

    val backButton = findViewById<ImageButton>(R.id.playlistBackButton)
        val cover = findViewById<ImageView>(R.id.playerAlbum)
        val trackName = findViewById<TextView>(R.id.trackNamePlayer)
        val artistName = findViewById<TextView>(R.id.artistNamePlayer)
        playButton = findViewById(R.id.playButton)
        val addTrack = findViewById<ImageButton>(R.id.addTrackPlaylist)
        val favorite = findViewById<ImageButton>(R.id.favorite)
        playbackProgress = findViewById(R.id.playbackProgress)
        val trackTime = findViewById<TextView>(R.id.trackTimeMillisModelTrack)
        val trackCollectionName = findViewById<TextView>(R.id.trackСollectionName)
        val trackReleaseDate = findViewById<TextView>(R.id.trackReleaseDate)
        val trackGenreName = findViewById<TextView>(R.id.trackGenreName)
        val trackCountry = findViewById<TextView>(R.id.trackCountry)
        val collection = findViewById<TextView>(R.id.collection)
        val trackReleaseYear = track.releaseDate?.substring(0..3)

        playButton.setOnClickListener {
            playbackControl()
        }

        track.let {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = dateFormat.format(track.trackTimeMillis)
            trackCollectionName.text = track.collectionName ?: ""
            trackReleaseDate.text = trackReleaseYear
            trackGenreName.text = track.primaryGenreName ?: ""
            trackCountry.text = track.country ?: ""
        }

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(cover)

        collection.isVisible = trackCollectionName.text.isNotEmpty()
        trackCollectionName.isVisible = trackCollectionName.text.isNotEmpty()

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        playerViewModel.playerState.observe(this) { state ->
            updateUI(state)
        }
        playerViewModel.currentPosition.observe(this) { position ->
            playbackProgress.text = dateFormat.format(position)
        }
    }

    private fun updateUI(state: PlayerStateStatus) {
        when (state) {
            PlayerStateStatus.STATE_PLAYING -> playButton.setImageResource(R.drawable.ic_pausebutton)
            PlayerStateStatus.STATE_PAUSED, PlayerStateStatus.STATE_PREPARED -> playButton.setImageResource(R.drawable.ic_playbutton)
            else -> {}
        }
    }

    private fun playbackControl() {
        when (playerViewModel.playerState.value) {
            PlayerStateStatus.STATE_PLAYING -> playerViewModel.pausePlayer()
            PlayerStateStatus.STATE_PREPARED, PlayerStateStatus.STATE_PAUSED -> playerViewModel.startPlayer()
            else -> {}
        }
    }
}