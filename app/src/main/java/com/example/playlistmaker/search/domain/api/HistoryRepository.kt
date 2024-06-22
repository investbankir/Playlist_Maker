package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun write(searchHistoryTrack: List<Track>)
    fun read(): List<Track>
    fun clearSearchHistory()
    fun addTrack(track: Track)
}