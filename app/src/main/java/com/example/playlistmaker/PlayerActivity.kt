package com.example.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val track = intent.getParcelableExtra<Track>(TRACK_DATA)

        val backButton = findViewById<ImageButton>(R.id.playlistBackButton)
        val cover = findViewById<ImageView>(R.id.playerAlbum)
        val trackName = findViewById<TextView>(R.id.trackNamePlayer)
        val artistName = findViewById<TextView>(R.id.artistNamePlayer)
        val playButton = findViewById<ImageButton>(R.id.playButton)
        val addTrack = findViewById<ImageButton>(R.id.addTrackPlaylist)
        val favorite = findViewById<ImageButton>(R.id.favorite)
        val playbackProgress = findViewById<TextView>(R.id.playbackProgress)
        val trackTime = findViewById<TextView>(R.id.trackTimeMillisModelTrack)
        val trackСollectionName = findViewById<TextView>(R.id.trackСollectionName)
        val trackReleaseDate = findViewById<TextView>(R.id.trackReleaseDate)
        val trackGenreName = findViewById<TextView>(R.id.trackGenreName)
        val trackCountry = findViewById<TextView>(R.id.trackCountry)
        val collection = findViewById<TextView>(R.id.collection)
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        val trackReleaseYear = track?.releaseDate?.substring(0..3)



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
}