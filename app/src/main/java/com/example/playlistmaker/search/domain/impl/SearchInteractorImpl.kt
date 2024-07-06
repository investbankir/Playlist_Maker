package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors


class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor{
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute{
            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {consumer.consume(foundTracks = resource.data as List<Track>?, isFailed = null)}
                is Resource.Error -> {consumer.consume(foundTracks = null, isFailed = resource.isFailed)}
            }
        }
    }
}