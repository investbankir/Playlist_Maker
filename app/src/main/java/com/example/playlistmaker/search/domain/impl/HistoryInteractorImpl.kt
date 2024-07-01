package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track



class HistoryInteractorImpl(
    private val repository : HistoryRepository) : HistoryInteractor {

    override fun getSearchHistory(): List<Track> = repository.read()

    override fun clearHistory() {
        repository.clearSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrack(track)
    }
}