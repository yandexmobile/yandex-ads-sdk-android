package com.yandex.ads.sample.tv.utils

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

fun Modifier.clickableWithNoIndication(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = null,
        indication = null,
        onClick = onClick
    )
}

fun Modifier.angledGradientBackground(
    colors: List<Color>,
    degrees: Float
) = this.drawWithCache {
    if (size.isEmpty()) return@drawWithCache onDrawBehind {}

    val rad = Math.toRadians((degrees % 360).let { if (it < 0) it + 360 else it }.toDouble()).toFloat()
    val cosA = cos(rad)
    val sinA = sin(rad)

    val halfW = size.width / 2f
    val halfH = size.height / 2f

    val halfLen = max(
        abs(halfW / (cosA.takeIf { abs(it) > 1e-4f } ?: Float.POSITIVE_INFINITY)),
        abs(halfH / (sinA.takeIf { abs(it) > 1e-4f } ?: Float.POSITIVE_INFINITY))
    )

    val dx = cosA * halfLen
    val dy = sinA * halfLen
    val start = Offset(halfW - dx, halfH - dy)
    val end = Offset(halfW + dx, halfH + dy)

    val brush = Brush.linearGradient(colors = colors, start = start, end = end)

    onDrawBehind {
        drawRect(brush = brush, size = size)
    }
}
