package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val WAITING_TIME = 400L
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private fun runTime() : Runnable{
        return object : Runnable{
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, WAITING_TIME)
                }
            }
        }
    }

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

        preparePlayer()



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

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        playbackProgress.text = getString(R.string.startPlayTime)
        mediaPlayer.apply {
            setDataSource(track?.previewUrl)
            prepareAsync()
            setOnPreparedListener{
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
                handler.removeCallbacks(runTime())
                playbackProgress.text = getString(R.string.startPlayTime)
                playButton.setImageResource(R.drawable.ic_playbutton)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pausebutton)
        playerState = STATE_PLAYING
        handler.post(runTime())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_playbutton)
        playerState = STATE_PAUSED
        handler.removeCallbacks(runTime())
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

}