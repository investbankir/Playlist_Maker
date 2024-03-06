package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Track

class trackListAdapter (
    private val trackList: ArrayList<Track>): RecyclerView.Adapter<trackViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): trackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return trackViewHolder(view)
    }
    override fun onBindViewHolder(holder: trackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
    override fun getItemCount(): Int {
        return trackList.size
    }
}
