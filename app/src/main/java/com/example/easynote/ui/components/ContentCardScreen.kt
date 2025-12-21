package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.easynote.viewmodels.TablesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navController: NavHostController,
    noteId: Int
) {
    val tablesViewModel: TablesViewModel = viewModel()
    val note by tablesViewModel.getNote(noteId).collectAsState(initial = null)

    var showSuccess by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ContentCardTopBar(
                title = "Detalle",
                onBack = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column {
                note?.let {
                    InfoImageCard(
                        note = it,
                        onDeleteClick = {
                            showDeleteDialog = true
                        }
                    )
                }
            }

            SuccessToast(
                message = "Eliminada correctamente",
                show = showSuccess
            )

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text("Eliminar nota")
                    },
                    text = {
                        Text("¿Seguro que deseas eliminar esta nota? Esta acción no se puede deshacer.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                note?.id?.let { id ->
                                    tablesViewModel.deleteNote(id)
                                }
                                showDeleteDialog = false
                                showSuccess = true

                                scope.launch {
                                    delay(500)
                                    navController.popBackStack()
                                }
                            }
                        ) {
                            Text(
                                "Eliminar",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
