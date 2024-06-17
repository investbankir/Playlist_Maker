package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.domain.interfaces.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.data.ResourcesProvider

class HistoryInteractorImpl(
    private val resourceProvider: ResourcesProvider
) : HistoryInteractor {
    override fun write(searchHistoryTrack: ArrayList<Track>) {
        TODO("Not yet implemented")
    }

    override fun read() {
        TODO("Not yet implemented")
    }

    override fun clearSearchHistory() {
        TODO("Not yet implemented")
    }

    override fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }

}