package com.yandex.ads.sample.tv.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.theme.ColorScheme.Background
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    animationDuration: Long = 1500L,
    delayBeforeFinish: Long = 200L
) {
    var initialized by remember { mutableStateOf(false) }

    val offsetY by animateDpAsState(
        targetValue = if (initialized) 0.dp else 75.dp,
        animationSpec = tween(animationDuration.toInt())
    )
    val scale by animateFloatAsState(
        targetValue = if (initialized) 1.2f else 0.4f,
        animationSpec = tween(animationDuration.toInt())
    )
    val alpha by animateFloatAsState(
        targetValue = if (initialized) 1f else 0f,
        animationSpec = tween(animationDuration.toInt())
    )

    LaunchedEffect(Unit) {
        initialized = true
        delay(animationDuration)
        delay(delayBeforeFinish)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.tv_splash_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(R.drawable.tv_yandex_ads_logo),
            contentDescription = stringResource(R.string.tv_yandex_ads),
            modifier = Modifier
                .offset(y = offsetY)
                .scale(scale)
                .alpha(alpha)
        )
    }
}
