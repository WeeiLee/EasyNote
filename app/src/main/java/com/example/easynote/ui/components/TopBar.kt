package com.example.easynote.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easynote.R
import com.example.easynote.ui.theme.EasyNoteTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewTask() {
    EasyNoteTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {

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
                MainSection(
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
                        .height(650.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_box_24),
                        contentDescription = "Empty Box",
                        modifier = Modifier.size(64.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    HoldToRecordButton(
                        onStart = {
                            // → Cuando empieza a presionar
                            Log.d("Record", "Iniciando grabación…")
                            // Aquí puedes iniciar AudioRecord() o tu lógica
                        },
                        onStop = {
                            // → Cuando suelta el botón
                            Log.d("Record", "Grabación detenida")
                            // Aquí paras la grabación
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TopScrollableTab() {

    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
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
