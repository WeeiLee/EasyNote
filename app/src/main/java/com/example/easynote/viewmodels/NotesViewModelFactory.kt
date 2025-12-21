package com.example.easynote.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotesViewModelFactory(private val tableId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(tableId) as T
    }
}
