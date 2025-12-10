package com.example.easynote.ui.main
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.audio.AudioViewModel
import com.example.easynote.service.local.chatGpt.ChatGptManager
import com.example.easynote.service.local.database.DatabaseManager
import com.example.easynote.ui.components.BottomBarWithHoldRecord
import com.example.easynote.ui.components.ChartViewer
import com.example.easynote.ui.components.ContentCard
import com.example.easynote.ui.components.DetailScreen
import com.example.easynote.ui.components.SuccessToast
import com.example.easynote.ui.components.TopScrollableTab
import com.example.easynote.ui.event.EventCreatorActivity
import com.example.easynote.ui.event.calendar.CalendarEvent
import com.example.easynote.ui.event.calendar.CalendarScreen
import com.example.easynote.ui.theme.EasyNoteTheme
import com.example.easynote.viewmodels.TablesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class MainActivity : ComponentActivity() {
    private val recordAudioRequest = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseManager.initialize(applicationContext) // Initialize database

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                recordAudioRequest
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Test(navController: NavHostController) {
    val context = LocalContext.current

    val audioViewModel: AudioViewModel = viewModel()
    val tablesViewModel: TablesViewModel = viewModel()

    val scope = rememberCoroutineScope()
    var showCalendar by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tables by tablesViewModel.tables.collectAsState()

    val eventTabIndex = 1 // "Event" is at index 1

    // Cargar eventos para el calendario
    var eventsForCalendar by remember { mutableStateOf(listOf<CalendarEvent>()) }
    LaunchedEffect(showCalendar) { 
        if (showCalendar) { // Refresh events when calendar is opened
            scope.launch {
                val loaded = DatabaseManager.getAllEvents()
                eventsForCalendar = loaded.map { entity ->
                    CalendarEvent(
                        id = entity.id,
                        title = entity.title,
                        description = entity.description,
                        date = Instant.ofEpochMilli(entity.timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    )
                }
            }
        }
    }

    val text by audioViewModel.text.collectAsState()
    val isListening by audioViewModel.isListening.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(text) {
        if (!isListening && text.isNotEmpty()) {
            Log.d("Listening", "EMPEZAR A PROCESAR")
            val note = processText(text, tables)
            tablesViewModel.addNote(note)
            showSuccess = true
            delay(1500)
            showSuccess = false
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
                onRecordStart = { audioViewModel.startRecording(context) },
                onRecordStop = { audioViewModel.stopRecording() },
                onLeftClick = { navController.navigate("pieChart") },
                onRightClick = { showCalendar = true },
                isEventTabSelected = selectedTab == eventTabIndex,
                isRecording = isListening,
                modifier = Modifier
            )
        }
    ) { innerPadding ->

        if (showCalendar) {
            ModalBottomSheet(
                onDismissRequest = { showCalendar = false },
                sheetState = sheetState
            ) {
                CalendarScreen(events = eventsForCalendar)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text)
                ContentCard(
                    selectedTab,
                    isListening,
                    navController
                )
            }
            SuccessToast("Nota agregada!", show = showSuccess)
        }
    }
}

suspend fun processText(input: String, tables: List<NoteTable>) : Note {
    val chatGptManager = ChatGptManager
    val response = chatGptManager.request(input, tables)
    Log.d("txt", response.summary)
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
        composable("home") { Test(navController) }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            DetailScreen(navController, id)
        }
        composable("pieChart") { ChartViewer(3, navController) }
    }
}
