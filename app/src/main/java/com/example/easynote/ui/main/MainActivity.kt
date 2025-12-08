package com.example.easynote.ui.main
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
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.chatGpt.ChatGptManager
import com.example.easynote.ui.components.BottomBarWithHoldRecord
import com.example.easynote.ui.components.SuccessToast
import com.example.easynote.viewmodels.TablesViewModel
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    private val RECORD_AUDIO_REQUEST = 100
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
                RECORD_AUDIO_REQUEST
            )
        }

        enableEdgeToEdge()
        setContent {
            EasyNoteTheme {
                Log.d("TEST", "ON START EJECUTADO")
                test()
            }
        }
    }
    }

    @Composable
    fun test(){
        val context = LocalContext.current

        val audioViewModel: AudioViewModel = viewModel()
        val tablesViewModel: TablesViewModel = viewModel()

        LaunchedEffect(Unit) {
            audioViewModel.configure(context)
        }
        val text by audioViewModel.text.collectAsState()
        val isListening by audioViewModel.isListening.collectAsState()
        var selectedTab by remember { mutableIntStateOf(0) }
        val tables by tablesViewModel.tables.collectAsState()

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
                    onRecordStart = {
                        audioViewModel.startRecording(context)
                        Log.d("---------------", text)},
                    onRecordStop = {audioViewModel.stopRecording()},
                    onRightClick = {Log.d("---------------", "right")}, //canlendar function
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
                    Text(text)
                    ContentCard(selectedTab)
                }

                SuccessToast("Nota agregada!",show = showSuccess)
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