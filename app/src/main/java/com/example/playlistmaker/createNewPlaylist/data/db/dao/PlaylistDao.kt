package com.example.playlistmaker.createNewPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.createNewPlaylist.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistsEntity): Long

    @Update(entity = PlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(plalist: PlaylistsEntity): Int

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): Flow<List<PlaylistsEntity>>

    @Query("SELECT * FROM playlists_table WHERE plalistId = :plalistId")
    fun getPlaylistById(plalistId: Long): PlaylistsEntity

    @Query("SELECT * FROM track_table ORDER BY addedTimestamp DESC")
    fun allTheTracksInThePlaylist(): Flow<List<TracksFromThePlaylistEntity>>

}