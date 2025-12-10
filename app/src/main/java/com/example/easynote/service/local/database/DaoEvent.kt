package com.example.easynote.service.local.database

import androidx.room.*

@Dao
interface DaoEvent {

    @Insert
    suspend fun insert(event: EventEntity): Long

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): EventEntity?

    @Query("SELECT * FROM events ORDER BY timestamp ASC")
    suspend fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE timestamp BETWEEN :start AND :end")
    suspend fun getEventsBetween(start: Long, end: Long): List<EventEntity>
}
