package com.example.easynote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.easynote.R
import com.example.easynote.ui.event.EventScreen

@Composable
fun HomeCompose() {
    Icon(
        painter = painterResource(id = R.drawable.outline_box_24),
        contentDescription = "Empty Box",
        modifier = Modifier.size(64.dp)
    )
}

@Composable
fun DotLottieAnimationPlayer(isRecording: Boolean) {
    val url =
        "https://lottie.host/0355fd56-e46c-49e3-9543-e243609a1271/M6xE0Cp9qa.lottie"
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(url)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (isRecording) LottieConstants.IterateForever else 1,
        speed = if (isRecording) 1f else 0f,
        isPlaying = isRecording
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(200.dp)
    )
}

@Composable
fun ContentCard(
    index: Int,
    isListening: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (index) {
            0 -> DotLottieAnimationPlayer(isListening)
            1 -> EventScreen() // <-- AQUÍ ESTÁ EL CAMBIO
            2 -> NoteListScreen(1, navController)
            3 -> NoteListScreen(2, navController)
            4 -> NoteListScreen(3, navController)
            5 -> NoteListScreen(4, navController)
        }
    }
}
