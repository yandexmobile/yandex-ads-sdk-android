package com.yandex.ads.sample.tv.start.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.tv.theme.ColorScheme.Primary
import com.yandex.ads.sample.tv.theme.ColorScheme.Secondary
import com.yandex.ads.sample.tv.theme.Typography.InstreamFormatNameTextStyle
import com.yandex.ads.sample.tv.utils.BoxWithFocusAnimatedBackground
import com.yandex.ads.sample.tv.utils.angledGradientBackground
import com.yandex.ads.sample.tv.utils.clickableWithNoIndication

@Composable
fun InstreamFormatCard(
    title: String,
    imageResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    val boxHeight by animateDpAsState(
        targetValue = if (isFocused) 134.dp else 112.dp,
    )

    val boxWidth by animateDpAsState(
        targetValue = if (isFocused) 230.dp else 192.dp,
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = if (isFocused) 0.dp else 12.dp,
    )

    BoxWithFocusAnimatedBackground(
        isFocused = isFocused,
        modifier = modifier
            .height(boxHeight)
            .width(boxWidth)
            .clip(RoundedCornerShape(12.dp))
            .onFocusChanged { isFocused = it.isFocused }
            .clickableWithNoIndication(onClick)
            .focusable(),
        unfocusedModifier = Modifier.background(Primary),
        focusedModifier = Modifier.angledGradientBackground(
            colors = listOf(Secondary, Primary),
            degrees = -20f
        )
    ) {
        Text(
            text = title,
            color = Color.White,
            style = InstreamFormatNameTextStyle,
            modifier = Modifier.padding(12.dp)
        )

        Image(
            painter = painterResource(imageResource),
            contentDescription = title,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .graphicsLayer {
                    scaleX = 0.65f
                    scaleY = 0.65f
                    transformOrigin = TransformOrigin(1f, 1f)
                }
                .offset(y = animatedOffsetY)
        )
    }
}
