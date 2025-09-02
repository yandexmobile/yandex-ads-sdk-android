package com.yandex.ads.sample.tv.utils

import android.text.format.DateUtils

fun formatTimestamp(timestampMs: Long): String {
    return DateUtils.formatElapsedTime(timestampMs / 1000)
}
