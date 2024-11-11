package com.example.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist

class BottomSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var playlistId: Long? = null
    private val playlistCover: ImageView = itemView.findViewById(R.id.playlistCover)
    private val playlistNameItem: TextView = itemView.findViewById(R.id.playlistNameItem)
    private val playlistQuantityItem: TextView = itemView.findViewById(R.id.playlistQuantityItem)

    fun bind(playlist: Playlist) {
        playlistId = playlist.playlistId

        Glide.with(itemView)
            .load(playlist.artworkUri)
            .placeholder(R.drawable.ic_player_placeholder)
            .transform(CenterCrop(), RoundedCorners(2))
            .into(playlistCover)
        playlistId = playlist.playlistId
        playlistNameItem.text = playlist.playlistName
        playlistQuantityItem.text = rightEnding(playlist.tracksList.size)
    }

    private fun rightEnding(quantityTracks: Int): String{
        val ending = when (quantityTracks % 10) {
            1 -> " трек"
            2, 3, 4, -> " трека"
            else -> " треков"
        }
        return quantityTracks.toString() + ending
    }
}