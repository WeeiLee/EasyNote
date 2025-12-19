package com.example.easynote.ui.components

import android.icu.number.IntegerWidth
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.easynote.R
import com.example.easynote.models.Note
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
                0 -> DotLottieAnimationPlayer(isListening)
                1 -> NoteListScreen(0, navController)
                2 -> NoteListScreen(1, navController)
                3 -> NoteListScreen(2, navController)
                4 -> NoteListScreen(3, navController)
                5 -> NoteListScreen(4, navController)
            }
        }
    }
}
