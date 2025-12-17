package com.example.easynote.service.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.easynote.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoNote {
    @Query("SELECT * FROM Notes")
    fun getNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM Notes WHERE noteTableId = :tableId ORDER BY timestamp DESC")
    fun getNotesByTableId(tableId: Int): Flow<List<Notes>>

    @Query("DELETE FROM Notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("DELETE FROM Notes WHERE noteTableId = :tableId")
    suspend fun deleteNotesByTableId(tableId: Int)

    @Query("SELECT * FROM Notes WHERE id = :noteId")
    fun getNoteById(noteId: Int) : Flow<Notes?>
    @Insert
    suspend fun insertNote(note: Notes): Long

    @Update
    suspend fun updateNote(note: Notes): Int

}