package com.example.playlistmaker.createNewPlaylist.domain.impl

import com.example.playlistmaker.createNewPlaylist.data.PlaylistDbConvertor
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistRepository
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistInteractor
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.player.data.TracksFromThePlaylistDbConverter
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class CreateNewPlaylistInteractorImpl(
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val repository: CreateNewPlaylistRepository
): CreateNewPlaylistInteractor {

    override fun addNewPlaylist(playlist: Playlist): Flow<Long> {
        val playlistsEntity = playlistDbConvertor.map(playlist)
        return repository.addNewPlaylist(playlistsEntity)
    }

    override fun updatePlaylist(playlist: Playlist): Flow<Int> {
        val playlistsEntity = playlistDbConvertor.map(playlist)
        return repository.updatePlaylist(playlistsEntity)
    }

    override fun saveCoverPlaylist(uri: String): String {
        return repository.saveCoverPlaylist(uri)
    }

    override fun addTrackInPlaylist(track: Track) {
        val tracksFromThePlaylist = TracksFromThePlaylistDbConverter().map(track)
        return repository.addTrackInPlaylist(tracksFromThePlaylist)
    }


}