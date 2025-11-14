package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
            0 -> Text("Event", modifier = Modifier.padding(16.dp))
            1 -> Text("Clock", modifier = Modifier.padding(16.dp))
            2 -> Text("Weight", modifier = Modifier.padding(16.dp))
            3 -> Text("Spending", modifier = Modifier.padding(16.dp))
            else -> Text("Others", modifier = Modifier.padding(16.dp))
        }
    }
}
