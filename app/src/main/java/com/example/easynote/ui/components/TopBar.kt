package com.example.easynote.ui.components

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.service.local.audio.AudioViewModel
import com.example.easynote.ui.theme.EasyNoteTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easynote.models.Audio
import com.example.easynote.models.Event
import kotlinx.coroutines.delay
import kotlin.text.toInt

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

    val audioViewModel: AudioViewModel = viewModel()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isMenuOpen = drawerState.isOpen
    var selectedTab by remember { mutableStateOf(0) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { AppDrawerContent() }
    ) {
        Scaffold(
            topBar = {
                TopScrollableTab(
                    selectedTab = selectedTab,
                    onTabSelected = { index -> selectedTab = index}
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                MenuScreen(
                    isMenuOpen = isMenuOpen,
                    index = selectedTab,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }
                )
                contentCard(selectedTab)

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    HoldToRecordButton(
                        onStart = {
                            audioViewModel.startRecording(context)
                        },
                        onStop = {
                            val path = audioViewModel.stopRecording()
                            //val content = processAudioToTxt(path)
                            //val event: Event = Event(content, Audio(path))
                        },
                        onClick = {
                            selectedTab = 0
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
fun TopScrollableTab(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    val tabs = listOf(
        "Home",
        "Event",
        "Clock",
        "Weight",
        "Spending",
        "Others"
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 16.dp
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}
