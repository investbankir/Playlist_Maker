package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun insertTrack(track: TrackEntity)
    suspend fun deleteTrack(track: TrackEntity)
    fun getTracks(): Flow<List<Track>>

     suspend fun onFavoriteClicked(track: Track)
    suspend fun isFavorite(trackId: Int): Boolean
}