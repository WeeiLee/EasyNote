package com.example.easynote.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.database.DatabaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TablesViewModel : ViewModel() {

    val tables: StateFlow<List<NoteTable>> =
        DatabaseManager.getTables()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val notes: StateFlow<List<Note>> =
        DatabaseManager.getAllNotes()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun createTable(table: NoteTable) {
        viewModelScope.launch {
            DatabaseManager.createTable(table)
        }
    }

    fun deleteTable(table: NoteTable) {
        viewModelScope.launch {
            DatabaseManager.deleteTable(table)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            DatabaseManager.addNote(note)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            DatabaseManager.deleteNote(id)
        }
    }

    fun getNote(id: Int): Flow<Note?> {
        return DatabaseManager.getNoteById(id)
    }



}
