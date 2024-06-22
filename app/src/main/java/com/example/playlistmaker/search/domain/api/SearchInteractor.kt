package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track


interface SearchInteractor {
    fun searchTracks(exception: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks : List<Track>?, isFailed : Boolean?)
    }
}