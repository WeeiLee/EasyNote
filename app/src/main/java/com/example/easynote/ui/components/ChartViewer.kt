package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    data: Map<String, Float>,
    navController: NavHostController
) {
    val notesViewModel: NotesViewModel = viewModel()
    val factory = remember { NotesViewModelFactory(tableId) }
    val vm: NotesViewModel = viewModel(
        key = "NotesVM_$tableId",
        factory = factory
    )

    val notes by vm.notes.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ContentCardTopBar(
                title = "Chart",
                onBack = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (notes.isNotEmpty()) {
                val result = notes.associate { note ->
                    val categoria = note.fields["Categoría"]?.toString() ?: ""
                    val cantidad  = note.fields["Cantidad"]?.toString()?.toFloatOrNull() ?: 0f
                    categoria to cantidad
                }
                PieChart(result)
            }
        }
    }
}