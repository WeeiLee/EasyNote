package com.example.easynote.service.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoTable {
    @Query("SELECT * FROM Tables")
    fun getTables(): Flow<List<Tables>>

    @Query("SELECT * FROM Tables WHERE id = :tableId")
    fun getTableById(tableId: Int): Flow<Tables?>

    @Query("DELETE FROM Tables WHERE id = :tableId")
    suspend fun deleteTableById(tableId: Int)

    @Insert
    suspend fun insertTable(table: Tables): Long
}