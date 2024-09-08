package com.example.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: TrackEntity)


    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)


    @Query("SELECT * FROM track_table ORDER BY addedTimestamp DESC")
    suspend fun getTracks(): List<TrackEntity>


    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Int>
}