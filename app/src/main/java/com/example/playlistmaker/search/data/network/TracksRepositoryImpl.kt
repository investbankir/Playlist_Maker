package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.search.domain.api.HistoryRepository

class TracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val historyRepository: HistoryRepository,
    private val networkClient: NetworkClient) : TracksRepository {
    override suspend fun searchTracks(expression: String) : Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(isFailed = false))
            } 200 -> {
            val favoriteListId = appDatabase.trackDao().getTracksId()
            emit(Resource.Success((response as TrackResponse).tracks.map {
                    Track(
                        it.trackId,
                        it.artworkUrl100,
                        it.trackName,
                        it.artistName,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.trackTimeMillis,
                        it.previewUrl,
                        isFavorite(it.trackId, favoriteListId),
                    )
                }))
            }
            else -> {
                emit(Resource.Error(isFailed = true))
            }
        }
    }

    override suspend fun processingSearchHistory(): Flow<ArrayList<Track>> = flow {
        val historyList = historyRepository.read()
        val favoriteListId = appDatabase.trackDao().getTracksId()
        if (favoriteListId.isNotEmpty()) historyList.forEach {
            it.isFavorite = isFavorite(it.trackId,favoriteListId) }
        emit(historyList as ArrayList<Track>) // Перепроверить почему не даёт просто historyList оставить
    }

    override suspend fun getFavoritesIdList(): Flow<List<Int>> = flow {
        emit(appDatabase.trackDao().getTracksId())
    }

    private fun isFavorite(trackId: Int?, favoriteListId: List<Int>): Boolean {
        val favorite = favoriteListId.find { it == trackId }
        return favorite != null
    }
}