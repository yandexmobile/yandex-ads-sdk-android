package com.yandex.ads.sample.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun applySystemBarsPadding(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { view, insets ->
        val systemBars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )

        view.setPadding(
            systemBars.left,
            systemBars.top,
            systemBars.right,
            systemBars.bottom
        )

        insets
    }
}
