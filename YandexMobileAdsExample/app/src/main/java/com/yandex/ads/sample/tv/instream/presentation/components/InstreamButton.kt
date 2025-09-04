package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonDisabled
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonFocused
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonTextDisabled
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonTextFocused
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonTextUnfocused
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamButtonUnfocused
import com.yandex.ads.sample.tv.theme.Typography.InstreamButtonTextStyle
import com.yandex.ads.sample.tv.utils.clickableWithNoIndication

@Composable
fun InstreamButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val contentColor by animateColorAsState(
        when {
            isFocused -> InstreamButtonTextFocused
            enabled -> InstreamButtonTextUnfocused
            else -> InstreamButtonTextDisabled
        }
    )
    val backgroundColor by animateColorAsState(
        when {
            isFocused -> InstreamButtonFocused
            enabled -> InstreamButtonUnfocused
            else -> InstreamButtonDisabled
        }
    )
    val focusModifier = if (enabled) {
        Modifier.onFocusChanged { isFocused = it.isFocused }
            .clickableWithNoIndication(onClick)
            .focusable()
    } else {
        Modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(percent = 100))
            .background(backgroundColor)
            .then(focusModifier)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = InstreamButtonTextStyle
        )
    }
}
