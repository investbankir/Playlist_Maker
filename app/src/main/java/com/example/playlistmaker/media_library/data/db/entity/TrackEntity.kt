package com.example.playlistmaker.media_library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Int,
    val artworkUrl100: String?,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate : String?,
    val primaryGenreName: String?,
    val country : String?,
    val trackTimeMillis: Long?,
    val previewUrl : String?,
    // Ставим дату добавления трэка в таблицу, по умолчанию текущее время
    val addedTimestamp: Long = System.currentTimeMillis()
)