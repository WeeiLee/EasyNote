package com.example.easynote.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easynote.models.Note

@Composable
fun EventListScreen(
    notes: List<Note>,
    onClick: (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        items(notes) { item ->
            noteItem(
                note = item,
                onClick = onClick,
                onDeleteClick = {
                    //todo
                }
            )
            NoteDivider()
        }
    }
}
