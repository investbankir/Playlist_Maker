package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow


interface SearchInteractor {
    suspend fun searchTracks(exception: String): Flow<Resource<List<Track>>>

}