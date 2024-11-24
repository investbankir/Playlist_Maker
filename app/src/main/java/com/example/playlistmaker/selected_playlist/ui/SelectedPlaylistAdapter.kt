package com.example.playlistmaker.selected_playlist.ui

import android.view.LayoutInflater
import com.example.playlistmaker.search.domain.models.Track
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.TrackViewHolder
import androidx.recyclerview.widget.RecyclerView

class SelectedPlaylistAdapter(
    private val clickListener: TrackClickListener,
    private val longClickListener: TrackLongClickListener): RecyclerView.Adapter<TrackViewHolder>() {

    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {clickListener.trackClicker(track)}
        holder.itemView.setOnLongClickListener { longClickListener.trackLongClicker(track)}
    }

    fun interface TrackClickListener {
        fun trackClicker(track: Track)
    }

    fun interface TrackLongClickListener {
        fun trackLongClicker(track: Track): Boolean
    }
}