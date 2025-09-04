package com.yandex.ads.sample.tv.start.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.theme.ColorScheme.PrimaryVariant
import com.yandex.ads.sample.tv.theme.ColorScheme.Secondary
import com.yandex.ads.sample.tv.theme.Typography.DemoButtonTextStyle
import com.yandex.ads.sample.tv.utils.BoxWithFocusAnimatedBackground
import com.yandex.ads.sample.tv.utils.angledGradientBackground
import com.yandex.ads.sample.tv.utils.clickableWithNoIndication

@Composable
fun DemoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(true) }
    val contentColor by animateColorAsState(
        if (isFocused) Color.Black else Color.White
    )

    BoxWithFocusAnimatedBackground(
        isFocused = isFocused,
        modifier = modifier
            .clip(RoundedCornerShape(percent = 100))
            .onFocusChanged { isFocused = it.isFocused }
            .clickableWithNoIndication(onClick)
            .focusable(),
        unfocusedModifier = Modifier.angledGradientBackground(
            colors = listOf(Secondary, PrimaryVariant),
            degrees = -15f
        ),
        focusedModifier = Modifier.background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 48.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.tv_play),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.tv_watch_demo),
                style = DemoButtonTextStyle,
                color = contentColor
            )
        }
    }
}
