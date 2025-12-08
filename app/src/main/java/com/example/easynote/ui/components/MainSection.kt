package com.example.easynote.ui.components

import android.icu.number.IntegerWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.easynote.R
import com.example.easynote.models.Note



@Composable
fun HomeCompose() {
    Icon(
        painter = painterResource(id = R.drawable.outline_box_24),
        contentDescription = "Empty Box",
        modifier = Modifier.size(64.dp)
    )
}


@Composable
fun ContentCard(
    index: Int,
    navController: NavHostController,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (index) {
            0 -> HomeCompose()
            1 -> NoteListScreen(0, navController)
            2 -> NoteListScreen(1, navController)
            3 -> NoteListScreen(2, navController)
            4 -> NoteListScreen(3, navController)
            5 -> NoteListScreen(4, navController)
        }
    }
}

