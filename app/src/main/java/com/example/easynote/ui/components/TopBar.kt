package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.easynote.service.local.audio.AudioViewModel
import com.example.easynote.ui.theme.EasyNoteTheme
import com.example.easynote.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easynote.models.Audio
import com.example.easynote.models.Event

//@Preview(showBackground = true)
@Composable
fun PreviewTask() {
    EasyNoteTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {

    val context = LocalContext.current

    val audioViewModel: AudioViewModel = viewModel(
        factory = ViewModelFactory(context)
    )


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isMenuOpen = drawerState.isOpen

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { AppDrawerContent() }
    ) {
        Scaffold(
            topBar = {
                TopScrollableTab()
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                MenuScreen(
                    isMenuOpen = isMenuOpen,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    HoldToRecordButton(
                        onStart = {
                            audioViewModel.startRecording()
                        },
                        onStop = {
                            val path = audioViewModel.stopRecording()
                            val content = processAudioToTxt(path)
                            val event: Event = Event(content, Audio(path))
                        },
                        onClick = {
                            // TODO
                        }
                    )
                }
            }
        }
    }
}

fun processAudioToTxt(path: String): String{
    return ""
}

@Composable
fun TopScrollableTab() {

    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        "Home",
        "Event",
        "Clock",
        "Weight",
        "Spending",
        "Others"
    )

    Column {

        // ---- TOP BAR con scroll ----
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // ---- CONTENIDO PRINCIPAL ----
        when (selectedTab) {
            0 -> {}
            1 -> {}
            2 -> {}
            3 -> {}
            else -> {}
        }
    }
}
