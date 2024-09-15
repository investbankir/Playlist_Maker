package com.example.playlistmaker.media_library.data

import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.domain.db.FavoriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.media_library.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor): FavoriteRepository {
    override suspend fun insertTrack(track: TrackEntity) {
        //CoroutineScope(Dispatchers.IO).launch{
            appDatabase.trackDao().insertTracks(track)
        //}
    }

    override suspend fun deleteTrack(track: TrackEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.trackDao().deleteTrackById(track.trackId)
        }
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
    override suspend fun onFavoriteClicked(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        if (track.isFavorite) {
            deleteTrack(trackEntity)
        } else {
            insertTrack(trackEntity)
        }
    }
    override suspend fun isFavorite(trackId: Int): Boolean {
        val favoriteTracks = getTracks().first() // или другой источник данных
        return favoriteTracks.any { it.trackId == trackId }
    }
}