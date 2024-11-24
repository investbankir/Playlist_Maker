package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository): PlaylistsInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun deleteTrackById(trackId: Int) {
        return repository.deleteTrackById(trackId)
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }

}