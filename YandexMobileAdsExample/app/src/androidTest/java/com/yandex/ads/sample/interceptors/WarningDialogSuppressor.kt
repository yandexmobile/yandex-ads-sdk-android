package com.yandex.ads.sample.interceptors

internal interface WarningDialogSuppressor {

    fun <T> suppressWarningDialog(activity: () -> T): T
}
