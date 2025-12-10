package com.example.easynote.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.easynote.ui.event.EventCreatorActivity

@Composable
fun BottomBarWithHoldRecord(
    onRecordStart: () -> Unit,
    onRecordStop: () -> Unit,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    isRecording: Boolean = false,
    isEventTabSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .height(80.dp),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onLeftClick) {
                Icon(Icons.Default.PieChart, "Gráfico")
            }

            // Botón central dinámico: cambia según la pestaña
            if (isEventTabSelected) {
                // Botón '+' para la pestaña de Eventos, con la lógica DENTRO
                IconButton(
                    onClick = {
                        val intent = Intent(context, EventCreatorActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear Evento",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                // Botón de micrófono para las otras pestañas
                Box(
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                        .background(
                            if (isRecording || isPressed) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    isPressed = true
                                    onRecordStart()
                                    tryAwaitRelease()
                                    isPressed = false
                                    onRecordStop()
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRecording || isPressed) Icons.Default.Stop
                        else Icons.Default.Mic,
                        contentDescription = if (isRecording || isPressed) "Grabando..."
                        else "Mantén para grabar",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            IconButton(onClick = onRightClick) {
                Icon(Icons.Default.CalendarMonth, "Calendario")
            }
        }
    }
}
