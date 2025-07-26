package com.example.mapster

import com.example.mapster.screens.MapsterPulseSplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        delay(2500)
        isVisible = false
        onSplashComplete()
    }

    if (isVisible) {
        MapsterPulseSplashScreen(
            imageRes = R.drawable.mapster_logo,
            onAnimationEnd = onSplashComplete
        )
    }
}