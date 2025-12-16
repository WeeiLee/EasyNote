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
                    Text(it, fontWeight = FontWeight.Bold)
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
                                        hasEvent -> Color(0xFFFFCDD2) // día con eventos
                                        isToday -> Color(0xFFBBDEFB)  // hoy
                                        else -> Color(0xFFF0F0F0)
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
                            date?.let { Text(it.dayOfMonth.toString()) }
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
                                color = Color.DarkGray
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        )
    }
}

@Composable
fun EventWidget(event: CalendarEvent, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE8EAF6)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.Bold)
                Text(event.description, color = Color.DarkGray)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, date: LocalDate, time: LocalTime, addToGoogleCalendar: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var showDatePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    var addToGoogleCalendar by remember { mutableStateOf(false) }


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onConfirm = { showTimePicker = false }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Evento") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )
                Spacer(Modifier.height(16.dp))

                val selectedDate = datePickerState.selectedDateMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                } ?: LocalDate.now()

                val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)


                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { showDatePicker = true }) {
                        Text("Fecha: $selectedDate")
                    }
                    Button(onClick = { showTimePicker = true }) {
                        Text("Hora: $selectedTime")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = addToGoogleCalendar, onCheckedChange = { addToGoogleCalendar = it })
                    Text("Añadir a Google Calendar")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    if (title.isNotBlank() && selectedDate != null) {
                        onConfirm(title, description, selectedDate, selectedTime, addToGoogleCalendar)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
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