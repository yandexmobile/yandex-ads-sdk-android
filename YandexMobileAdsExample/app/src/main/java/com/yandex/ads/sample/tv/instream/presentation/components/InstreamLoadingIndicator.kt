package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.tv.theme.ColorScheme.Primary

@Composable
fun InstreamLoadingIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(96.dp),
        color = Primary,
        strokeWidth = 6.dp
    )
}
