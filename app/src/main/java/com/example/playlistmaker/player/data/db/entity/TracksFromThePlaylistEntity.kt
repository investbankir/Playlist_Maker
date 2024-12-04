package com.example.playlistmaker.player.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tracksFromThePlaylist_table")
data class TracksFromThePlaylistEntity(
    @PrimaryKey(autoGenerate = false)
    val trackId: Int,
    val artworkUrl100: String?,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: Long?,
    val previewUrl: String?,
    val addedTimestamp: Long// = System.currentTimeMillis()
)
