package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTracks(): Flow<List<Track>>

     suspend fun onFavoriteClicked(track: Track)
    suspend fun isFavorite(trackId: Int): Boolean
}