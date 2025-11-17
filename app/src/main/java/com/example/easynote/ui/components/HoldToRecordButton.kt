package com.example.easynote.ui.components

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.easynote.R
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HoldToRecordButton(
    onStart: () -> Unit,
    onStop: () -> Unit,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    var downTime by remember { mutableLongStateOf(0L) }

    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(
                if (pressed) Color.Red else MaterialTheme.colorScheme.primary
            )
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressed = true
                        downTime = System.currentTimeMillis()
                        onStart()
                    }

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> {
                        pressed = false
                        onStop()

                        val elapsed = System.currentTimeMillis() - downTime
                        if (elapsed < 200) {
                            onClick()
                        }
                    }
                }
                true
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.voice_button),
            contentDescription = "Record",
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )
    }
}
