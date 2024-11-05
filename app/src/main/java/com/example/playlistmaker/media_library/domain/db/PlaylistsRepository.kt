package com.example.playlistmaker.media_library.domain.db

import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track

interface PlaylistsRepository {
    suspend fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    fun getTracksFromPlaylists(playlistIdList: List<Int>): Flow<List<Track>>
}