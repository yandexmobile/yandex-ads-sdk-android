package com.yandex.ads.sample.utils

import android.content.Context
import kotlin.math.roundToInt

object ScreenUtil {

    val Context.screenWidth: Int
        get() = resources.displayMetrics.run { widthPixels / density }.roundToInt()

    val Context.screenHeight: Int
        get() = resources.displayMetrics.run { heightPixels / density }.roundToInt()
}
