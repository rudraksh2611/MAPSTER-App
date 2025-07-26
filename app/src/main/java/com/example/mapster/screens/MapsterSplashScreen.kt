package com.example.mapster.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapsterPulseSplashScreen(
    imageRes: Int,
    onAnimationEnd: () -> Unit
) {
    var isAnimationPlayed by remember { mutableStateOf(false) }

    val pulse = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(true) {
        coroutineScope {
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(500)
                )
            }
            launch {
                pulse.animateTo(
                    targetValue = 1.2f,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
                pulse.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
        }
        delay(1200)
        isAnimationPlayed = true
        onAnimationEnd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = pulse.value,
                    scaleY = pulse.value,
                    alpha = alpha.value
                )
        )
    }
}