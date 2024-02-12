package com.yandex.ads.sample.base

import com.kaspersky.kaspresso.params.FlakySafetyParams
import io.reactivex.exceptions.ExtCompositeException

internal val FlakySafetyParams.Companion.defaultExt: Set<Class<out Throwable>> by lazy {
    FlakySafetyParams
        .Companion
        .defaultAllowedExceptions
        .plus(ExtCompositeException::class.java)
}
