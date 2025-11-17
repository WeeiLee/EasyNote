package com.example.easynote.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import com.example.easynote.R

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    isMenuOpen: Boolean,
    onMenuClick: () -> Unit
) {
    Column(Modifier) {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = if (isMenuOpen)
                    Icons.Outlined.Close
                else
                    Icons.Outlined.Menu,
                contentDescription = "Menú"
            )
        }
        HomeCompose()
    }
}

@Composable
fun AppDrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        Text("Inicio")
        Spacer(modifier = Modifier.height(12.dp))
        Text("Perfil")
        Spacer(modifier = Modifier.height(12.dp))
        Text("Ajustes")
    }
}

@Composable
fun HomeCompose() {
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
}