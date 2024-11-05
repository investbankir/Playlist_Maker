package com.example.playlistmaker.media_library.ui.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import android.view.LayoutInflater
import com.example.playlistmaker.R
class PlaylistAdapter(private val clickListener: PlaylistClickListener): RecyclerView.Adapter<PlaylistViewHolder>()  {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { clickListener.playlistClick(playlist.playlistId)}
    }
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
    fun interface PlaylistClickListener {
        fun playlistClick(playlistId: Long)
    }
}