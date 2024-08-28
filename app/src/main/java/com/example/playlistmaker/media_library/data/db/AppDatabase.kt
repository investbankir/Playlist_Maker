package com.example.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.data.db.dao.TrackDao


@Database(
    version = 1,
    entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}