package com.yandex.ads.sample.tv.instream.player.model

import androidx.annotation.StringRes
import com.yandex.ads.sample.R

enum class ErrorType(
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    CONTENT(R.string.tv_content_error_title, R.string.tv_error_description),
    AD(R.string.tv_ad_error_title, R.string.tv_error_description)
}
