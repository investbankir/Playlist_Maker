package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter (
    private val context : Context,
    var trackList: ArrayList<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            SearchHistory.addTrack(trackList[position])
            notifyDataSetChanged()

            val playerIntent = Intent(context, PlayerActivity::class.java)
            playerIntent.putExtra("track", trackList[position])
            context.startActivity(playerIntent)
        }
    }
    override fun getItemCount(): Int {
        return trackList.size
    }
}
