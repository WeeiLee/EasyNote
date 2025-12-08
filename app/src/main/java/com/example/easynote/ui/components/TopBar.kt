package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easynote.ui.theme.EasyNoteTheme

//@Preview(showBackground = true)
@Composable
fun PreviewTask() {
    EasyNoteTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {

    /*val context = LocalContext.current

    val audioViewModel: AudioViewModel = viewModel()
    LaunchedEffect(Unit) {
        audioViewModel.configure(context)
    }
    audioViewModel.configure(context)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isMenuOpen = drawerState.isOpen
    val text by audioViewModel.text.collectAsState()
    val isListening by audioViewModel.isListening.collectAsState()
    */
}

fun processAudioToTxt(path: String): String{
    return ""
}

@Composable
fun TopScrollableTab(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        "Home",
        "Event",
        "Clock",
        "Weight",
        "Spending",
        "Others"
    )

    Column(modifier = modifier) {

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
}

