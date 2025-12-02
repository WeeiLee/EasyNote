package com.example.easynote.ui.main
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easynote.models.Audio
import com.example.easynote.models.Event
import com.example.easynote.models.FieldType
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.NoteDatabaseManager
import com.example.easynote.service.local.audio.AudioViewModel
import com.example.easynote.service.local.chatGpt.ChatGptManager
import com.example.easynote.service.local.database.DatabaseManager
import com.example.easynote.ui.components.AppDrawerContent
import com.example.easynote.ui.components.HoldToRecordButton
import com.example.easynote.ui.components.MenuScreen
import com.example.easynote.ui.components.TopScrollableTab
import com.example.easynote.ui.components.contentCard
import com.example.easynote.ui.components.processAudioToTxt
import com.example.easynote.ui.theme.EasyNoteTheme
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log



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

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val isMenuOpen = drawerState.isOpen
        val text by audioViewModel.text.collectAsState()
        val isListening by audioViewModel.isListening.collectAsState()
        var selectedTab by remember { mutableStateOf(0) }



        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { AppDrawerContent() }
        ) {
            Scaffold(
                topBar = {
                    TopScrollableTab(
                        selectedTab = selectedTab,
                        onTabSelected = { index -> selectedTab = index}
                    )
                }
            ) { innerPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    MenuScreen(
                        isMenuOpen = isMenuOpen,
                        index = selectedTab,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }
                    )
                    Text(text)

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        HoldToRecordButton(
                            onStart = {
                                audioViewModel.startRecording(context)
                                Log.d("-------onstart--------", text)
                            },
                            onStop = {
                                audioViewModel.stopRecording()
                                Log.d("---------------", text)


                            },
                            onClick = {
                                selectedTab = 0
                                Log.d("---------onclick------", text)
                                Log.d("---------jajaja------", text)
                            }
                        )
                    }
                }
            }
        }
    }

