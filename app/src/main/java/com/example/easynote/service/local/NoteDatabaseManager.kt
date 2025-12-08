package com.example.easynote.service.local

import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.database.DatabaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NoteDatabaseManager(private var noteTable: NoteTable) {

    private val databaseManager: DatabaseManager = DatabaseManager

    suspend fun createIfNotExists(): Boolean {

        // Obtener las tablas una sola vez
        val tables = databaseManager.getTables().first()

        // Si ya existe una tabla con el mismo título
        tables.firstOrNull { it.title == noteTable.title }?.let {
            noteTable = it
            return true
        }

        // Si la tabla no tiene ID → crear nueva
        if (noteTable.id == null) {
            val newId = if (tables.isEmpty()) 1 else tables.maxOf { it.id ?: 0 } + 1

            val newTable = NoteTable(newId, noteTable.title, noteTable.description, noteTable.types)
            val tableId = databaseManager.createTable(newTable)

            // Obtener la tabla creada
            noteTable = databaseManager.getTables().first().first { it.id == tableId.toInt() }

            return true
        }

        // Si la tabla tiene ID pero no existe en BD → crear
        if (!tables.any { it.id == noteTable.id }) {
            return true
        }

        return false
    }

    fun getNoteTable(): NoteTable = noteTable

    fun getNotes(): Flow<List<Note>> {
        return databaseManager.getNotesByTableID(noteTable.id)
    }

    suspend fun addNote(note: Note) {
        databaseManager.addNote(note)
    }

    suspend fun deleteNote(id: Int) {
        databaseManager.deleteNote(id)
    }

    suspend fun deleteTable() {
        databaseManager.deleteTable(noteTable)
    }
}
