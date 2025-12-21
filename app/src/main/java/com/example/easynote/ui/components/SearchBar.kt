import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.easynote.models.Note


@Composable
fun NotesSearchWithResults(
    notes: List<Note>,
    onNoteSelected: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }

    val results = remember(query, notes) {
        if (query.isBlank()) emptyList()
        else notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.summary.contains(query, ignoreCase = true)
        }
    }

    Column(modifier = modifier) {

        // 🔍 Barra de búsqueda
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Buscar notas…") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar")
                    }
                }
            },
            shape = RoundedCornerShape(20.dp)
        )

        // 📋 Resultados justo debajo
        AnimatedVisibility(visible = results.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 240.dp)
                ) {
                    results.take(5)
                    items(results) { note ->
                        SearchResultItem(
                            note = note,
                            onClick = {
                                onNoteSelected(note)
                                query = ""
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SearchResultItem(
    note: Note,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        if (note.summary.isNotBlank()) {
            Text(
                text = note.summary,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}
