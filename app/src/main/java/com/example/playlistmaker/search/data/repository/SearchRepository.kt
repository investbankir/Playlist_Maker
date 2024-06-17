package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchActivity

interface SearchRepository {
    fun searchTrack()
    fun addTrack(track: Track)
    fun showResult(result : SearchActivity.SearchForResults)
}