package com.example.playlistmaker.media_library.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.data.db.dao.TrackDao


@Database(entities = [TrackEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "database.db"
                )
                    .addMigrations(MIGRATION_1_2) // Добавляем миграцию
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
    }
}