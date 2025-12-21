package com.example.easynote.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.easynote.viewmodels.NotesViewModel
import com.example.easynote.viewmodels.NotesViewModelFactory

@Composable
fun NoteListScreen(
    tableId: Int,
    navController: NavHostController,
    ) {
    val factory = remember { NotesViewModelFactory(tableId) }
    val vm: NotesViewModel = viewModel(
        key = "NotesVM_$tableId",
        factory = factory
    )

    val notes by vm.notes.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        items(notes) { item ->
            NoteItem(
                note = item,
                onClick = {
                    id -> navController.navigate("detail/${id}")
                }
            )
            NoteDivider()
        }
    }
}
