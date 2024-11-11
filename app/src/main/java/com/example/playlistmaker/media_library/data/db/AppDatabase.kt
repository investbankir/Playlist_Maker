package com.example.playlistmaker.media_library.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.createNewPlaylist.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.media_library.data.db.dao.TrackDao
import com.example.playlistmaker.createNewPlaylist.data.db.dao.PlaylistDao
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity
import com.example.playlistmaker.player.data.db.dao.TracksFromThePlaylistDao


@Database(entities = [TrackEntity::class, PlaylistsEntity::class, TracksFromThePlaylistEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracksFromThePlaylistDao(): TracksFromThePlaylistDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Добавляем миграцию
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE track_table ADD COLUMN addedTimestamp LONG DEFAULT ${System.currentTimeMillis()}")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS playlists_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS tracks_from_playlist_table (id INTEGER PRIMARY KEY AUTOINCREMENT, playlistId INTEGER NOT NULL, trackId INTEGER NOT NULL)"
                )
            }
        }
    }
}