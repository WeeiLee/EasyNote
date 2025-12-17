package com.example.easynote.ui.main
import NotesSearchWithResults
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easynote.service.local.audio.AudioViewModel
import com.example.easynote.service.local.database.DatabaseManager
import com.example.easynote.ui.components.TopScrollableTab
import com.example.easynote.ui.components.ContentCard
import com.example.easynote.ui.theme.EasyNoteTheme
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.chatGpt.ChatGptManager
import com.example.easynote.ui.components.BottomBarWithHoldRecord
import com.example.easynote.ui.components.CalendarViewer
import com.example.easynote.ui.components.ChartViewer
import com.example.easynote.ui.components.DetailScreen
import com.example.easynote.ui.components.SuccessToast
import com.example.easynote.ui.components.setReminder
import com.example.easynote.viewmodels.TablesViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    private val RECORD_AUDIO_REQUEST = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseManager.initialize(applicationContext) // Initialize database
        createEventChannel(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        enableEdgeToEdge()
        setContent {
            EasyNoteTheme {
                Log.d("TEST", "ON START EJECUTADO")
                AppNavigation()
            }
        }
    }
    }

    @Composable
    fun MenuScreen(navController: NavHostController) {
        val context = LocalContext.current

        val audioViewModel: AudioViewModel = viewModel()
        val tablesViewModel: TablesViewModel = viewModel()

        LaunchedEffect(Unit) {
            audioViewModel.configure(context)
        }
        val text by audioViewModel.text.collectAsState()
        val isListening by audioViewModel.isListening.collectAsState()
        var selectedTab by rememberSaveable { mutableIntStateOf(0) }
        val tables by tablesViewModel.tables.collectAsState()
        val notes by tablesViewModel.notes.collectAsState()
        var showSuccess by remember { mutableStateOf(false) }
        var message by remember { mutableStateOf("") }
        var isSuccess by remember { mutableStateOf(true) }
        var startTime by remember { mutableLongStateOf(0L) }
        var isValidRecording by remember { mutableStateOf(true) }



        LaunchedEffect(isListening) {
            if (!isListening && startTime != 0L) {
                val duration = SystemClock.elapsedRealtime() - startTime

                if (duration < 1500) {
                    message = "Mensaje demasiado corto"
                    isSuccess = false
                    isValidRecording = false
                    showSuccess = true
                    delay(1500)
                    showSuccess = false
                    return@LaunchedEffect
                }
            }
        }


        LaunchedEffect(text, isListening) {
            if (!isListening &&
                text.isNotEmpty() &&
                text != audioViewModel.lastProcessedText &&
                isValidRecording
            ) {
                val duration = SystemClock.elapsedRealtime() - startTime
                if (duration < 1500) return@LaunchedEffect
                Log.d("texto a procesar", "TEXTO: $text")
                message = "Generando su nota..."
                isSuccess = true
                showSuccess = true
                delay(1000)
                showSuccess = false

                val note = processText(text, tables)
                tablesViewModel.addNote(note)
                setNotification(context, note)
                message = "Nota agregada correctamente"
                showSuccess = true
                delay(1500)
                showSuccess = false
                audioViewModel.lastProcessedText = text
            }
        }


        Scaffold(
            contentWindowInsets = WindowInsets(0),
            topBar = {
                TopScrollableTab(
                    selectedTab = selectedTab,
                    onTabSelected = { index -> selectedTab = index },
                    modifier = Modifier.statusBarsPadding()
                )
            },
            bottomBar = {
                BottomBarWithHoldRecord(
                    onRecordStart = {
                        isValidRecording = true
                        startTime = SystemClock.elapsedRealtime()
                        audioViewModel.startRecording(context) },
                    onRecordStop = {
                        Log.d("stop----", "stop")
                        audioViewModel.stopRecording() },
                    onLeftClick = {navController.navigate("pieChart")},
                    onRightClick = {navController.navigate("calendar")}, //canlendar function
                    modifier = Modifier
                )
            }

        )  { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NotesSearchWithResults(
                        notes = notes,
                        onNoteSelected = { note ->
                            navController.navigate("detail/${note.id}")
                            Log.d("SEARCH", "Seleccionada: ${note.title}")
                        },
                        modifier = Modifier.padding(8.dp)
                    )

                    ContentCard(
                        selectedTab,
                        isListening,
                        navController
                        )
                }

                SuccessToast(message,show = showSuccess, success = isSuccess)
            }
        }

    }

suspend fun processText(input: String, tables: List<NoteTable>) : Note {
    val chatGptManager = ChatGptManager
    val response = chatGptManager.request(input, tables)
    Log.d("txt", response.fields.toString())
    return Note(
        null,
        response.title,
        input,
        response.summary,
        response.fields,
        response.tableId,
        response.timestamp,
    )
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { MenuScreen(navController) }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            DetailScreen(navController, id)
        }
        composable("pieChart") { ChartViewer(3, navController) }
        composable("calendar") { CalendarViewer(0, navController) }
    }
}

fun createEventChannel(context: Context) {
    val channel = NotificationChannel(
        "event_channel",
        "Recordatorios de eventos",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Notificaciones de eventos del calendario"
    }

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}

fun setNotification(context: Context, note: Note) {
    val eventTableId = 0
    val clockTableId = 1
    if (note.noteTableId == eventTableId) {

        val date = note.fields["Fecha"]
            ?.toString()
            ?.trim()
            ?.let {
                try {
                    when {
                        it.contains("T") -> LocalDateTime.parse(it).toLocalDate()
                        else -> LocalDate.parse(it)
                    }
                } catch (e: Exception) {
                    LocalDate.now()
                }
            } ?: LocalDate.now()

        setReminder(context, date, note.title, note.summary)
    }
    else if (note.noteTableId == clockTableId) {

        val rawTime = note.fields["Hora"]
            ?.toString()
            ?.trim()

        if (rawTime.isNullOrEmpty()) return

        try {
            val dateTime = LocalDateTime.parse(rawTime)

            setReminder(
                context = context,
                date = dateTime.toLocalDate(),
                title = note.title,
                description = note.summary,
                hour = dateTime.hour,
                minute = dateTime.minute
            )

        } catch (e: Exception) {
            Log.e("Reminder", "Hora inválida: $rawTime")
        }
    }
}