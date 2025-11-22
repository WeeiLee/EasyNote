package com.example.easynote.service.local.database

import android.content.Context
import androidx.room.Room
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable

object DatabaseManager {
    private lateinit var database: DaoDatabase

    fun initialize(context: Context) {
        if (!::database.isInitialized) {
            database = Room.databaseBuilder(
                context.applicationContext,
                DaoDatabase::class.java,
                "easy_note_database"
            ).fallbackToDestructiveMigration().build() // TODO : delete fallbackToDestructiveMigration in production
        }
    }

    suspend fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        for (note in database.noteDao().getNotes()) {
            notes.add(createNoteFromEntity(note))
        }
        return notes
    }

    suspend fun getNotesByTable(noteTable: NoteTable): List<Note> {
        val notes = mutableListOf<Note>()
        for (note in database.noteDao().getNotesByTableId(noteTable.id)) {
            notes.add(createNoteFromEntity(note))
        }
        return notes
    }

    suspend fun getTableFromNote(note: Note): NoteTable? {
        val tableEntity = database.tableDao().getTableById(note.noteTableId)
        return tableEntity?.let { createTableFromEntity(it) }
    }

    suspend fun deleteNote(note: Note) {
        database.noteDao().deleteNoteById(note.id)
    }


    suspend fun getTables(): List<NoteTable> {
        val tables = mutableListOf<NoteTable>()
        for (table in database.tableDao().getTables()) {
            tables.add(createTableFromEntity(table))
        }
        return tables
    }
    private suspend fun deleteNotesByTable(table: NoteTable) {
        database.noteDao().deleteNotesByTableId(table.id)
    }

    suspend fun deleteTable(table: NoteTable) {
        deleteNotesByTable(table)
        database.tableDao().deleteTableById(table.id)
    }

    suspend fun addNote(note: Note): Long {
        val tableTypes = getTableFromNote(note)?.types?.keys?.toSet() ?: emptySet()

        val fieldKeys = note.fields?.keys?.toSet() ?: emptySet()

        if (!fieldKeys.all { it in tableTypes }) {
            println("DEBUG tableTypes = $tableTypes")
            println("DEBUG fieldKeys = $fieldKeys")
            throw IllegalArgumentException("Fields contain keys not defined in the table's types.")
        }

        val entity = Notes(
            id = note.id,
            title = note.title,
            originalContent = note.originalContent,
            summary = note.summary,
            fields = note.fields,
            noteTableId = note.noteTableId,
            timestamp = note.timestamp
        )

        return database.noteDao().insertNote(entity)
    }

    suspend fun createTable(noteTable: NoteTable): Long {
        val entity = Tables(
            id = noteTable.id,
            title = noteTable.title,
            description = noteTable.description,
            types = noteTable.types
        )
        return database.tableDao().insertTable(entity)
    }

    suspend fun updateNote(note:  Note): Boolean {
        val entity = Notes(
            id = note.id,
            title = note.title,
            originalContent = note.originalContent,
            summary = note.summary,
            fields = note.fields,
            noteTableId = note.noteTableId,
            timestamp = note.timestamp
        )
        return database.noteDao().updateNote(entity) == 1
    }

    private fun createTableFromEntity(table: Tables): NoteTable {
        return NoteTable(
            table.id,
            table.title,
            table.description,
            table.types
        )
    }

    private fun createNoteFromEntity(note: Notes): Note {
        return Note(
            note.id,
            note.title,
            note.summary,
            note.originalContent,
            note.fields,
            note.noteTableId,
            note.timestamp,
            )
    }

}