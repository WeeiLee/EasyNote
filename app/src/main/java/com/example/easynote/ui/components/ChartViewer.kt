package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.easynote.viewmodels.NotesViewModel
import com.example.easynote.viewmodels.NotesViewModelFactory
import com.example.easynote.viewmodels.TablesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChartViewer(
    tableId: Int,
    navController: NavHostController
) {
    val expenseId: Int = 3
    val tablesViewModel: TablesViewModel = viewModel()
    val notes by tablesViewModel.notes.collectAsState()

    Scaffold(
        topBar = {
            ContentCardTopBar(
                title = "Chart",
                onBack = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            if (notes.isEmpty()) {
                EmptyStateMessage()
                return@Column
            }

            val chartData = notes
                .filter { it.noteTableId == expenseId }
                .associate { note ->
                    val categoria = note.fields["Categoría"]?.toString().orEmpty()
                    val cantidad = note.fields["Cantidad"]?.toString()?.toFloatOrNull() ?: 0f
                    categoria to cantidad
                }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Distribución de gasto",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PieChart(
                        data = chartData,
                        modifier = Modifier.size(280.dp)
                    )
                }
            }

        }
    }
}
@Composable
fun EmptyStateMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No hay datos para mostrar aún")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Añade notas de gastos'",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
