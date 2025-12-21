package com.example.easynote.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.models.CalendarEvent
import com.example.easynote.models.Note
import com.example.easynote.service.local.database.DatabaseManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class NotesViewModel(private val tableId: Int) : ViewModel() {

    val notes: StateFlow<List<Note>> =
        DatabaseManager.getNotesByTableID(tableId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    val events: StateFlow<List<CalendarEvent>> =
        notes
            .map { notesList ->
                notesList.map { it.toCalendarEvent() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
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
    private fun Note.toCalendarEvent(): CalendarEvent {
        return CalendarEvent(
            id = id,
            title = title,
            description = summary,
            date = convertToLocalDate(this)
        )
    }

    private fun convertToLocalDate(note: Note): LocalDate {
        val dateField = note.fields["Fecha"]?.toString()?.trim()
        return try {
            when {
                dateField.isNullOrEmpty() -> {
                    LocalDateTime.parse(note.timestamp).toLocalDate()
                }
                dateField.contains("T") -> LocalDateTime.parse(dateField).toLocalDate()
                else -> {
                    LocalDate.parse(dateField)
                }
            }
        } catch (e: Exception) {
            LocalDate.now()
        }
    }
}
