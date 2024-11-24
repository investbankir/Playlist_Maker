package com.example.playlistmaker.createNewPlaylist.domain.db

import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CreateNewPlaylistInteractor {
    fun addNewPlaylist(playlist: Playlist): Flow<Long>
    fun updatePlaylist(playlist: Playlist): Flow<Int>
    fun saveCoverPlaylist(uri: String): String
    fun addTrackInPlaylist(track: Track)
    fun deletePlaylistById(playlistId: Long): Flow<Long>

}