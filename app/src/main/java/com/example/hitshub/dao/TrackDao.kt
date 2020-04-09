package com.example.hitshub.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hitshub.models.Track

@Dao
interface TrackDao {
    @Insert
    fun insert(track: Track)

    @Query("DELETE FROM liked_track WHERE id = :id")
    fun delete(id: String)

    @Query("SELECT * FROM liked_track")
    fun selectAllTracks(): List<Track>

    @Query("SELECT * FROM liked_track WHERE id = :id")
    fun selectTrack(id: String): Track
}
