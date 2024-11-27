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
    fun updatePlaylist(playlist: PlaylistsEntity): Int

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): Flow<List<PlaylistsEntity>>

    @Query("SELECT * FROM playlists_table WHERE plalistId = :plalistId")
    fun getPlaylistById(plalistId: Long): PlaylistsEntity

    @Query("SELECT * FROM tracksFromThePlaylist_table ORDER BY addedTimestamp DESC")
    fun allTheTracksInThePlaylist(): Flow<List<TracksFromThePlaylistEntity>>

    @Query("DELETE FROM tracksFromThePlaylist_table WHERE trackId = :trackId")
    fun deleteTrackById (trackId:Int)
    @Query("DELETE FROM playlists_table WHERE plalistId = :playlistId")
    fun deletePlaylistById (playlistId:Long)
}