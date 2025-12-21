package com.example.easynote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.easynote.models.CalendarEvent
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


@Composable
fun CalendarScreen(
    events: List<CalendarEvent>) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val today = LocalDate.now()

    val eventsByDate = remember(events) { events.groupBy { it.date } }
    val markedDays = remember(eventsByDate) { eventsByDate.keys }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedDayEvents by remember { mutableStateOf<List<CalendarEvent>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior")
            }

            Text(
                currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth()) {
            listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom").forEach {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        it,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
            }
        }

        Spacer(Modifier.height(6.dp))

        val first = currentMonth.atDay(1)
        val last = currentMonth.atEndOfMonth()

        val startOffset = first.dayOfWeek.value - 1
        var dayNumber = 1 - startOffset

        Column {
            repeat(6) {
                Row(Modifier.fillMaxWidth()) {
                    repeat(7) {
                        val date =
                            if (dayNumber in 1..last.dayOfMonth) currentMonth.atDay(dayNumber)
                            else null

                        val isToday = date == today
                        val hasEvent = date != null && markedDays.contains(date)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .background(
                                    when {
                                        hasEvent -> Color(0xFFD25D5D)
                                        isToday -> MaterialTheme.colorScheme.primaryContainer
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable(enabled = hasEvent) {
                                    val d = date!!
                                    selectedDate = d
                                    selectedDayEvents = eventsByDate[d].orEmpty()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            date?.let {
                                Text(
                                    it.dayOfMonth.toString(),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                        }

                        dayNumber++
                    }
                }
            }
        }
    }

    if (selectedDate != null && selectedDayEvents.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                selectedDate = null
                selectedDayEvents = emptyList()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = null
                        selectedDayEvents = emptyList()
                    }
                ) { Text("Cerrar") }
            },
            title = {
                val d = selectedDate!!
                Text("Eventos del ${d.dayOfMonth}/${d.monthValue}/${d.year}")
            },
            text = {
                Column {
                    Text("${selectedDayEvents.size} evento(s)")
                    Spacer(Modifier.height(8.dp))

                    selectedDayEvents.forEach { ev ->
                        Text("• ${ev.title}", fontWeight = FontWeight.SemiBold)
                        if (ev.description.isNotBlank()) {
                            Text(
                                ev.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(title: String = "Select Time", onDismissRequest: () -> Unit, onConfirm: () -> Unit, content: @Composable () -> Unit) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
                )
                content()
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) { Text("Cancelar") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}