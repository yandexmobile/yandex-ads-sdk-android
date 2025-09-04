package com.yandex.ads.sample.tv.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    background = ColorScheme.Background,
    primary = ColorScheme.Primary,
    primaryContainer = ColorScheme.PrimaryVariant,
    secondary = ColorScheme.Secondary,
)

@Composable
fun TVTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
