package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.annotation.OptIn
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonUnfocused
import com.yandex.ads.sample.tv.utils.BoxWithFocusAnimatedBackground
import com.yandex.ads.sample.tv.utils.clickableWithNoIndication

@OptIn(UnstableApi::class)
@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (isPlaying) {
        painterResource(R.drawable.tv_pause)
    } else {
        painterResource(R.drawable.tv_play)
    }
    val contentDescription = if (isPlaying) {
        stringResource(R.string.tv_pause)
    } else {
        stringResource(R.string.tv_play)
    }

    var isFocused by remember { mutableStateOf(true) }

    val iconTint by animateColorAsState(
        if (isFocused) Color.Black else Color.White
    )

    BoxWithFocusAnimatedBackground(
        isFocused = isFocused,
        unfocusedModifier = Modifier.background(InstreamButtonUnfocused),
        focusedModifier = Modifier
            .background(Color.White.copy(alpha = 0.65f))
            .background(Color.Black.copy(alpha = 0.15f)),
        modifier = modifier
            .size(96.dp)
            .clip(RoundedCornerShape(percent = 100))
            .onFocusChanged { isFocused = it.isFocused }
            .clickableWithNoIndication(
                onClick = if (isPlaying) onPause else onPlay
            )
            .focusable(),
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(32.dp).align(Alignment.Center)
        )
    }
}
