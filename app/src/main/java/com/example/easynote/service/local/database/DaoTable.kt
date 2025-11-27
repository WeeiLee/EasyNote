package com.example.easynote.service.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoTable {
    @Query("SELECT * FROM Tables")
    suspend fun getTables(): List<Tables>

    @Query("SELECT * FROM Tables WHERE id = :tableId")
    suspend fun getTableById(tableId: Int): Tables?

    @Query("DELETE FROM Tables WHERE id = :tableId")
    suspend fun deleteTableById(tableId: Int)

    @Insert
    suspend fun insertTable(table: Tables): Long
}