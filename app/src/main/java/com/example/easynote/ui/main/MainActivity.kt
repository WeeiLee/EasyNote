package com.example.easynote.ui.main
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.easynote.models.FieldType
import com.example.easynote.models.Note
import com.example.easynote.models.NoteTable
import com.example.easynote.service.local.NoteDatabaseManager
import com.example.easynote.service.local.chatGpt.ChatGptManager
import com.example.easynote.service.local.database.DatabaseManager
import com.example.easynote.ui.theme.EasyNoteTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseManager.initialize(applicationContext) // Initialize database

        enableEdgeToEdge()
        setContent {
            EasyNoteTheme {
                MainScreen()
            }
        }
    }
}
