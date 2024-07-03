package com.yandex.ads.sample.pageobjects

import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.pageobjects.dialog.DefaultDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.DialogScreen
import com.yandex.ads.sample.settings.GdprDialogFragment
import com.google.android.material.R as MaterialR

internal class GdprDialogScreen(
    private val dialogScreen: DialogScreen = DefaultDialogScreen(
        titleTextViewId = MaterialR.id.alertTitle,
        acceptButtonId = android.R.id.button1,
        declineButtonId = android.R.id.button2,
        neutralButtonId = android.R.id.button3
    )
) : KScreen<GdprDialogScreen>(),
    DialogScreen by dialogScreen {

    override val layoutId: Int? = null

    override val viewClass: Class<*> = GdprDialogFragment::class.java
}
