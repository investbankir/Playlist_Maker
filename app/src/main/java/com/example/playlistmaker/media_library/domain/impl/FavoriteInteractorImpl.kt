package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.db.FavoriteInteractor
import com.example.playlistmaker.media_library.domain.db.FavoriteRepository
import com.example.playlistmaker.media_library.data.TrackDbConvertor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
    private val trackDbConvertor: TrackDbConvertor): FavoriteInteractor {
    override suspend fun insertTrack(trackEntity: TrackEntity) {
        favoriteRepository.insertTrack(trackEntity)
    }

    override suspend fun deleteTrack(trackEntity: TrackEntity) {
        favoriteRepository.deleteTrack(trackEntity)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoriteRepository.getTracks()
    }

    override suspend fun onFavoriteClicked(track: Track) {
        return favoriteRepository.onFavoriteClicked(track)
    }
    override suspend fun isFavorite(trackId: Int): Boolean {
        return favoriteRepository.isFavorite(trackId)
    }
}