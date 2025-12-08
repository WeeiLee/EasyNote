package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    navController: NavHostController, noteId: Int) {
    val tablesViewModel: TablesViewModel = viewModel()
    val note by tablesViewModel.getNote(noteId).collectAsState(initial = null)
    var showSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            ContentCardTopBar(
                title = "Detalle",
                onBack = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (note != null) {
                InfoImageCard(
                    note,
                    onDeleteClick = { id ->
                        tablesViewModel.deleteNote(id)

                        showSuccess = true
                        scope.launch {
                            delay(1200)
                            navController.popBackStack()
                        }
                    }
                )
            }
            SuccessToast("Eliminada correctamente", showSuccess)
        }
    }
}

