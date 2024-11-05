package com.example.playlistmaker.createNewPlaylist.domain.db

import com.example.playlistmaker.createNewPlaylist.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity
import kotlinx.coroutines.flow.Flow


interface CreateNewPlaylistRepository {
    fun addNewPlaylist(playlist: PlaylistsEntity): Flow<Long>
    fun updatePlaylist(playlist: PlaylistsEntity): Flow<Int>
    fun saveCoverPlaylist(uri: String): String
    fun addTrackInPlaylist(track: TracksFromThePlaylistEntity)

}