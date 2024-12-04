package com.example.playlistmaker.createNewPlaylist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistsEntity(
    @PrimaryKey(autoGenerate = true)
    val plalistId: Long,
    val playlistName: String?,
    val playlistDescription: String?,
    val artworkUri: String?,
    val tracksList: String?,
    val quantityTracks: Int?,
    val totalPlaylistTime: Int,
    val endingMinute: String = "минут"
)
