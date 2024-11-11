package com.example.playlistmaker.createNewPlaylist.data

import com.google.gson.Gson
import com.example.playlistmaker.createNewPlaylist.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist

class PlaylistDbConvertor(private val gson: Gson) {
    fun map(playlist: Playlist): PlaylistsEntity {
        return PlaylistsEntity(
        playlist.playlistId,
        playlist.playlistName,
        playlist.playlistDescription,
        playlist.artworkUri,
        gson.toJson(playlist.tracksList),
        playlist.quantityTracks
        )
    }

    fun map(playlist: PlaylistsEntity): Playlist {
        return Playlist(
            playlist.plalistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.artworkUri,
            gson.fromJson(playlist.tracksList, Array<Int>::class.java).toCollection(ArrayList()),
            playlist.quantityTracks
        )
    }
}