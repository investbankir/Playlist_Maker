package com.example.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity
@Dao
interface   TracksFromThePlaylistDao {
    @Insert(entity = TracksFromThePlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrackFromPlaylist(track: TracksFromThePlaylistEntity)
}