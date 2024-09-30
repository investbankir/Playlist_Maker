package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow


interface TracksRepository {
    suspend fun searchTracks (expression: String) : Flow<Resource<List<Track>>>
    suspend fun processingSearchHistory(): Flow<ArrayList<Track>>
    suspend fun getFavoritesIdList(): Flow<List<Int>>
}