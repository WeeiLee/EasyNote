package com.example.easynote.service.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoNote {
    @Query("SELECT * FROM Notes")
    suspend fun getNotes(): List<Notes>

    @Query("SELECT * FROM Notes WHERE noteTableId = :tableId")
    suspend fun getNotesByTableId(tableId: Int): List<Notes>

    @Query("DELETE FROM Notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("DELETE FROM Notes WHERE noteTableId = :tableId")
    suspend fun deleteNotesByTableId(tableId: Int)

    @Insert
    suspend fun insertNote(note: Notes): Long

    @Update
    suspend fun updateNote(note: Notes): Int

}