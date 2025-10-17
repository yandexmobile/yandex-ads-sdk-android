package com.yandex.ads.sample.tv.instream.player.model

sealed interface ContentType {
    object Content : ContentType
    object Ad : ContentType
    data class Error(val type: ErrorType) : ContentType
}
