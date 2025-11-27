package com.example.easynote.service.local

import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.database.DatabaseManager

class NoteDatabaseManager {

    private var noteTable: NoteTable
    private val databaseManager: DatabaseManager = DatabaseManager

    constructor(noteTable: NoteTable) {
        this.noteTable = noteTable
    }

    suspend fun createIfNotExists(): Boolean {
        val tables = databaseManager.getTables()

        for (table in tables) {
            if (table.title == noteTable.title) {
                noteTable = table
                return true
            }
        }

        if (this.noteTable.id == null) {
            val newId = if (tables.isEmpty()) 1 else tables.maxOf { it.id ?: 0 } + 1
            val newTable = NoteTable(newId, noteTable.title, noteTable.description, noteTable.types)
            val tableId = databaseManager.createTable(newTable)
            noteTable = databaseManager.getTables().first { it.id == tableId.toInt() }
            return true
        }

        if (!tables.any { it.id == noteTable.id }) {
            return true
        }

        return false
    }

    fun getNoteTable(): NoteTable {
        return noteTable
    }

    suspend fun getNotes(): List<Note> {
        return databaseManager.getNotesByTable(noteTable).toMutableList()
    }

    suspend fun addNote(note: Note) {
        databaseManager.addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        databaseManager.deleteNote(note)
    }

    suspend fun deleteTable() {
        databaseManager.deleteTable(noteTable)
    }
}