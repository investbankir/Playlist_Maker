package com.example.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.ui.search.TRACK_DATA
import com.example.playlistmaker.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.models.PlayerStateStatus
import com.example.playlistmaker.domain.repository.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {
    companion object {
        private const val WAITING_TIME = 400L
    }
    private val playerRepository : PlayerRepository by lazy {PlayerRepositoryImpl()}
        private var playerState = PlayerStateStatus.STATE_DEFAULT
        private val handler = Handler(Looper.getMainLooper())
        private lateinit var track: Track
        private lateinit var playbackProgress: TextView
        private lateinit var playButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        track = intent.getParcelableExtra(TRACK_DATA)!!


        val backButton = findViewById<ImageButton>(R.id.playlistBackButton)
        val cover = findViewById<ImageView>(R.id.playerAlbum)
        val trackName = findViewById<TextView>(R.id.trackNamePlayer)
        val artistName = findViewById<TextView>(R.id.artistNamePlayer)
        playButton = findViewById(R.id.playButton)
        val addTrack = findViewById<ImageButton>(R.id.addTrackPlaylist)
        val favorite = findViewById<ImageButton>(R.id.favorite)
        playbackProgress = findViewById(R.id.playbackProgress)
        val trackTime = findViewById<TextView>(R.id.trackTimeMillisModelTrack)
        val trackСollectionName = findViewById<TextView>(R.id.trackСollectionName)
        val trackReleaseDate = findViewById<TextView>(R.id.trackReleaseDate)
        val trackGenreName = findViewById<TextView>(R.id.trackGenreName)
        val trackCountry = findViewById<TextView>(R.id.trackCountry)
        val collection = findViewById<TextView>(R.id.collection)
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        val trackReleaseYear = track?.releaseDate?.substring(0..3)

        playButton.setOnClickListener{
            playbackControl()
        }

        playerRepository.preparePlayer(track.previewUrl)
        playerRepository.setOnChangePlayerListener { state ->
            playerState = state
            updateUI(state)
        }

        track?.let {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = dateFormat.format(track.trackTimeMillis)
            trackСollectionName.text = track.collectionName ?: ""
            trackReleaseDate.text = trackReleaseYear
            trackGenreName.text = track.primaryGenreName ?: ""
            trackCountry.text = track.country ?: ""
        }


        Glide.with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(cover)

        collection.isVisible = trackСollectionName.text.isNotEmpty()
        trackСollectionName.isVisible = trackСollectionName.text.isNotEmpty()


        backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun runTime() : Runnable{
        return object : Runnable{
            override fun run() {
                if (playerState == PlayerStateStatus.STATE_PLAYING) {
                    playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerRepository.getCurrentPosition())
                    handler.postDelayed(this, WAITING_TIME)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerRepository.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerRepository.releasePlayer()
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateUI(state: PlayerStateStatus){
        when(state) {
            PlayerStateStatus.STATE_PLAYING -> {
                playButton.setImageResource(R.drawable.ic_pausebutton)
                handler.post (runTime())
            }
            PlayerStateStatus.STATE_PAUSED, PlayerStateStatus.STATE_PREPARED -> {
                playButton.setImageResource(R.drawable.ic_playbutton)
                handler.removeCallbacks(runTime())
            }
            else -> {}
        }
    }
    private fun playbackControl() {
        when(playerState) {
            PlayerStateStatus.STATE_PLAYING -> {
                playerRepository.pausePlayer()
            }
            PlayerStateStatus.STATE_PREPARED, PlayerStateStatus.STATE_PAUSED -> {
                playerRepository.startPlayer()
            }

            else -> {}
        }
    }

}