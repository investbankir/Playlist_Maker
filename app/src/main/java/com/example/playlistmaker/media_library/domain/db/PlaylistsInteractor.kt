package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    fun deleteTrackById (trackId:Int)
    fun getPlaylistById(playlistId: Long): Flow<Playlist>


}