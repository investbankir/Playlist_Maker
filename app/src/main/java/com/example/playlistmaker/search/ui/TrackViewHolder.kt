package com.example.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerFragment
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_DATA = "track"
class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTimeMillis)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.album)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val clickingOnTheTrack:View = itemView.findViewById(R.id.clickingOnTheTrack)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeMillis.text = dateFormat.format(track.trackTimeMillis)



        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkUrl100)

        clickingOnTheTrack.setOnClickListener {
            val navController = it.findNavController()
            val bundle = PlayerFragment.createArgs(track)
            navController.navigate(R.id.action_searchFragment_to_playerFragment, bundle)
        }
    }
}