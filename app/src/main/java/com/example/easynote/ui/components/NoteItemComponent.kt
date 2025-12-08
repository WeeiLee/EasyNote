package com.example.easynote.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.easynote.models.Note

@Composable
fun NoteItem(
    note: Note,
    onClick: (Int) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onClick(note.id)}
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        // Title
        Text(
            text = note.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        // summary
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = note.summary,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        // time
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = note.timestamp,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray.copy(alpha = 0.6f)
        )
    }
}

