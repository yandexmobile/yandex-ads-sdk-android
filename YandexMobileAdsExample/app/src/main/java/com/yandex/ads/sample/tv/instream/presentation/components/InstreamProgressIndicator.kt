package com.yandex.ads.sample.tv.instream.presentation.components

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.tv.theme.ColorScheme.ProgressTrackColor
import kotlin.math.roundToInt

@Composable
fun InstreamProgressIndicator(
    progress: () -> Float,
    seekBack: () -> Unit,
    seekForward: () -> Unit,
    color: Color = Color.White,
    trackColor: Color = ProgressTrackColor,
    focusCircleColor: Color = Color.White,
    focusCircleSize: Dp = 12.dp,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var isFocused by remember { mutableStateOf(false) }
    var trackWidth by remember { mutableIntStateOf(0) }

    val progressWidth by remember {
        derivedStateOf {
            val progress = progress().coerceIn(0f, 1f)
            val pixels = if (trackWidth <= 0) 0 else (trackWidth * progress).roundToInt()
            with(density) { pixels.toDp() }
        }
    }
    val focusCircleOffset = remember(progressWidth) {
        with(density) { progressWidth - focusCircleSize / 2 }
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { trackWidth = it.width }
            .height(12.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            seekBack()
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            seekForward()
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            .focusable()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(percent = 100))
                .background(trackColor)
        )

        Box(
            modifier = Modifier
                .width(progressWidth)
                .height(4.dp)
                .clip(RoundedCornerShape(percent = 100))
                .background(color)
        )

        if (isFocused) {
            Box(
                modifier = Modifier
                    .size(focusCircleSize)
                    .offset(x = focusCircleOffset)
                    .background(focusCircleColor, CircleShape)
            )
        }
    }
}
