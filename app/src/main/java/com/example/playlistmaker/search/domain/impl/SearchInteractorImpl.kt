package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override suspend fun searchTracks(expression: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Resource.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    Resource.Error(result.isFailed ?: false, result.data ?: emptyList())
                }
            }
        }
    }
}