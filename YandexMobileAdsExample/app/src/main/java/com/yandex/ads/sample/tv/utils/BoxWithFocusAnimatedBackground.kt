package com.yandex.ads.sample.tv.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BoxWithFocusAnimatedBackground(
    isFocused: Boolean,
    modifier: Modifier = Modifier,
    unfocusedModifier: Modifier = Modifier,
    focusedModifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {

        Box(modifier = unfocusedModifier.matchParentSize())

        AnimatedVisibility(
            visible = isFocused,
            modifier = Modifier.matchParentSize()
        ) {
            Box(modifier = focusedModifier)
        }

        content()
    }
}
