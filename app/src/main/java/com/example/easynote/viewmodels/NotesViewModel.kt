package com.example.easynote.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.models.Note
import com.example.easynote.service.local.database.DatabaseManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val tableId: Int) : ViewModel() {

    val notes: StateFlow<List<Note>> =
        DatabaseManager.getNotesByTableID(tableId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun addNote(note: Note) {
        viewModelScope.launch {
            DatabaseManager.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            DatabaseManager.updateNote(note)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            DatabaseManager.deleteNote(id)
        }
    }
}
