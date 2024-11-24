package com.example.playlistmaker.selected_playlist.domain.impl

import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.selected_playlist.domain.api.SelectedPlaylistInteractor
import kotlinx.coroutines.flow.Flow

class SelectedPlaylistInteractorImpl(private val repository: PlaylistsRepository): SelectedPlaylistInteractor {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }

    override fun getTracksFromPlaylists(playlistIdList: List<Int>): Flow<List<Track>> {
        return repository.getTracksFromPlaylists(playlistIdList)
    }

    override fun deletePlaylistById(playlistId: Long) {
        return repository.deletePlaylistById(playlistId)
    }
}