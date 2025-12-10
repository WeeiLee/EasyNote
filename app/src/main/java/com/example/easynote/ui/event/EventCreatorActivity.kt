package com.example.easynote.ui.event

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.easynote.service.local.database.DatabaseManager
import com.example.easynote.service.local.database.EventEntity
import com.example.easynote.service.reminder.ReminderManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EventCreatorActivity : ComponentActivity() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    private val NOTIFICATION_REQUEST = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        lifecycleScope.launch {
            val event = if (eventId != -1) DatabaseManager.getEventById(eventId) else null
            setContent {
                EventCreatorScreen(event)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_REQUEST
            )
        }
    }

    @Composable
    fun EventCreatorScreen(existingEvent: EventEntity?) {
        val calendar = Calendar.getInstance()
        existingEvent?.let {
            calendar.timeInMillis = it.timestamp
        }

        var title by remember { mutableStateOf(existingEvent?.title ?: "") }
        var desc by remember { mutableStateOf(existingEvent?.description ?: "") }
        var location by remember { mutableStateOf(existingEvent?.location ?: "") }

        val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        var date by remember { mutableStateOf(existingEvent?.let { dateFormat.format(calendar.time) } ?: "Seleccionar fecha") }
        var time by remember { mutableStateOf(existingEvent?.let { timeFormat.format(calendar.time) } ?: "Seleccionar hora") }

        // Validación para que los campos sean obligatorios
        val isFormValid = title.isNotBlank() && date != "Seleccionar fecha" && time != "Seleccionar hora"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Ubicación (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                DatePickerDialog(
                    this@EventCreatorActivity,
                    { _, y, m, d ->
                        calendar.set(Calendar.YEAR, y)
                        calendar.set(Calendar.MONTH, m)
                        calendar.set(Calendar.DAY_OF_MONTH, d)
                        date = dateFormat.format(calendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) { Text(date) }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                TimePickerDialog(
                    this@EventCreatorActivity,
                    { _, h, min ->
                        calendar.set(Calendar.HOUR_OF_DAY, h)
                        calendar.set(Calendar.MINUTE, min)
                        time = timeFormat.format(calendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }) { Text(time) }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    val action = {
                        lifecycleScope.launch {
                            if (existingEvent != null) {
                                // Update
                                val updatedEvent = existingEvent.copy(
                                    title = title,
                                    description = desc,
                                    timestamp = calendar.timeInMillis,
                                    location = location
                                )
                                DatabaseManager.updateEvent(updatedEvent)
                                Toast.makeText(this@EventCreatorActivity, "Evento actualizado", Toast.LENGTH_SHORT).show()
                            } else {
                                // Create
                                val newEvent = EventEntity(
                                    title = title,
                                    description = desc,
                                    timestamp = calendar.timeInMillis,
                                    location = location
                                )
                                DatabaseManager.saveEvent(
                                    title = newEvent.title,
                                    description = newEvent.description,
                                    timestamp = newEvent.timestamp,
                                    location = newEvent.location
                                )
                                Toast.makeText(this@EventCreatorActivity, "Evento guardado", Toast.LENGTH_SHORT).show()
                            }
                        }

                        ReminderManager.scheduleReminder(
                            this@EventCreatorActivity,
                            title,
                            desc,
                            calendar.timeInMillis
                        )
                        
                        setResult(Activity.RESULT_OK)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (!alarmManager.canScheduleExactAlarms()) {
                            Intent().also { intent ->
                                intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                                startActivity(intent)
                            }
                            Toast.makeText(this@EventCreatorActivity, "Por favor, concede el permiso para alarmas", Toast.LENGTH_LONG).show()
                        } else {
                            action()
                        }
                    } else {
                        action()
                    }
                },
                enabled = isFormValid, // <-- CAMBIO PRINCIPAL
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (existingEvent == null) "Crear Recordatorio Interno" else "Actualizar Evento")
            }

            if (existingEvent == null) {
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_INSERT).apply {
                            data = CalendarContract.Events.CONTENT_URI
                            putExtra(CalendarContract.Events.TITLE, title)
                            putExtra(CalendarContract.Events.DESCRIPTION, desc)
                            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
                            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis + 3600000)
                        }
                        startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir a Google Calendar")
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = { finish() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Volver al menú")
            }
        }
    }
}
