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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.easynote.models.Note
import com.example.easynote.viewmodels.TablesViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ChartViewer(
    tableId: Int,
    navController: NavHostController
) {
    val expenseId: Int = 3
    val weightId: Int = 2
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

            val pieChartData = processPieChartData(notes, expenseId)

            val lineChartData = processLineChartData(notes, weightId)


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
                        data = pieChartData,
                        modifier = Modifier.size(280.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Seguimiento de peso",
                        style = MaterialTheme.typography.titleMedium
                    )
                    LineChart(lineChartData)
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

fun processPieChartData(notes: List<Note>, expenseId: Int): Map<String, Float> {
    return notes
        .filter { it.noteTableId == expenseId }
        .associate { note ->
            val category = note.fields["Categoría"]
                ?.toString()
                ?.lowercase()
                .orEmpty()

            val amount = note.fields["Cantidad"]?.toString()?.toFloatOrNull() ?: 0f
            category to amount
        }
}

fun processLineChartData(notes: List<Note>, weightId: Int): Map<LocalDate, Float> {
    return notes
        .filter { it.noteTableId == weightId }
        .mapNotNull { note ->

            val date = note.fields["Fecha"]
                ?.toString()
                ?.let {
                    try {
                        when {
                            it.contains("T") -> LocalDateTime.parse(it).toLocalDate()
                            else -> LocalDate.parse(it)
                        }
                    } catch (e: Exception) {
                        null
                    }
                }

            val weight = note.fields["Peso (kg)"]
                ?.toString()
                ?.toFloatOrNull()

            if (date != null && weight != null) {
                date to weight
            } else {
                null
            }
        }
        .toMap()


}