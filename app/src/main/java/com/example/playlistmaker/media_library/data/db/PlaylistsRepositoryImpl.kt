package com.example.playlistmaker.media_library.data.db

import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.createNewPlaylist.data.PlaylistDbConvertor
import com.example.playlistmaker.player.data.TracksFromThePlaylistDbConverter
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConvertor,
    private val tracksFromThePlaylistDbConverter: TracksFromThePlaylistDbConverter): PlaylistsRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { emptyList ->
            emptyList.map { convertor.map(it)}
        }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        emit(convertor.map(playlist))
    }

    override fun getTracksFromPlaylists(playlistIdList: List<Int>): Flow<List<Track>> {
        return appDatabase.playlistDao().allTheTracksInThePlaylist().map { listEntity ->
            tracksFilter(listEntity, playlistIdList)
        }
    }
    private fun tracksFilter (listEntity: List<TracksFromThePlaylistEntity>,  playlistIdList: List<Int>): List<Track> {
        val result = mutableListOf<Track>()
        for (track: TracksFromThePlaylistEntity in listEntity) if (playlistIdList.any { it == track.trackId})
            result.add(tracksFromThePlaylistDbConverter.map(track))
        return result
    }


    override fun deleteTrackById(trackId: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            appDatabase.playlistDao().deleteTrackById(trackId)
        }
    }

    override fun deletePlaylistById(playlistId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.playlistDao().deletePlaylistById(playlistId)
        }
    }
}