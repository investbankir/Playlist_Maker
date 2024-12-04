package com.example.playlistmaker.createNewPlaylist.domain.models

import java.io.Serializable

data class Playlist(
    val playlistId: Long,
    val playlistName: String?,
    val playlistDescription: String?,
    val artworkUri: String?,
    val tracksList: MutableList<Int>,
    val quantityTracks: Int?,
    val totalPlaylistTime: Int,
    val endingMinute: String = "минут"
): Serializable
