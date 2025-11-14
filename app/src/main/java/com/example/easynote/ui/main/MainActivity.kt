package com.example.easynote.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.service.remote.ChatGptService
import com.example.easynote.ui.theme.EasyNoteTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    // 🔹 Estado reactivo
    private val messageState = mutableStateOf("Hola, bienvenido a EasyNote!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EasyNoteTheme {
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        MyButton {
                            lifecycleScope.launch {
                                sendRequest()
                            }
                        }
                        ShowMessage(messageState.value)
                    }
                }
            }
        }
    }

    // 🔹 Función suspend que actualiza el estado
    private suspend fun sendRequest() {
        messageState.value = "Esperando respuesta de ChatGPT..."
        try {
            val response = ChatGptService.request("¿Cómo está el clima en Gran Canarias?")
            messageState.value = ChatGptService.getText(response)
        } catch (e: Exception) {
            messageState.value = "Error: ${e.message}"
        }
    }
}


@Composable
fun MyButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("ChatGPT")
    }
}

@Composable
fun ShowMessage(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(top = 50.dp)
    )
}