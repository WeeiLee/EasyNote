package com.example.easynote.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.easynote.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*


@Composable
fun HomeCompose() {
    Icon(
        painter = painterResource(id = R.drawable.outline_box_24),
        contentDescription = "Empty Box",
        modifier = Modifier.size(64.dp)
    )
}

@Composable
fun NoteDotLottieAnimationPlayer(isRecording: Boolean) {
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
fun ChristmasDotLottieAnimationPlayer(
    isRecording: Boolean,
    modifier: Modifier = Modifier
) {
    val url =
        "https://lottie.host/285ea034-b76f-4e9b-87f8-f132eca0a40f/sOznSI5UMa.lottie"

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(url)
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
        modifier = modifier
    )
}


@Composable
fun CombinedLottieColumn(
    isRecording: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        NoteDotLottieAnimationPlayer(
            isRecording = isRecording
        )

        // Empuja la animación de Navidad hacia abajo
        Spacer(modifier = Modifier.weight(1f))

        ChristmasDotLottieAnimationPlayer(
            isRecording = isRecording,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(bottom = 12.dp)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentCard(
    index: Int,
    onIndexChange: (Int) -> Unit,
    isListening: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val pageCount = 6

    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { pageCount }
    )

    // 🔁 Swipe → index
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != index) {
            onIndexChange(pagerState.currentPage)
        }
    }

    // 🔁 index → swipe (cuando cambias tab)
    LaunchedEffect(index) {
        if (index != pagerState.currentPage) {
            pagerState.scrollToPage(index)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) { page ->

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (page) {
                0 -> CombinedLottieColumn(
                    isRecording = isListening,
                    modifier = Modifier.fillMaxWidth()
                )
                1 -> NoteListScreen(0, navController)
                2 -> NoteListScreen(1, navController)
                3 -> NoteListScreen(2, navController)
                4 -> NoteListScreen(3, navController)
                5 -> NoteListScreen(4, navController)
            }
        }
    }
}
