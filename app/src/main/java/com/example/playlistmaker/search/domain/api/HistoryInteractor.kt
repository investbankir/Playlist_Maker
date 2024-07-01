package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track


interface HistoryInteractor {
    fun getSearchHistory(): List<Track>
    fun clearHistory()
    fun addTrackToHistory(track: Track)
}