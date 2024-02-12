package com.yandex.ads.sample.pageobjects

import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.pageobjects.dialog.AcceptableDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.DeclinableDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.DefaultDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.DialogScreen
import com.yandex.ads.sample.pageobjects.dialog.HasTitleDialogScreen
import com.yandex.ads.sample.settings.CoppaDialogFragment

internal class CoppaDialogScreen(
    private val dialogScreen: DialogScreen = DefaultDialogScreen(
        titleTextViewId = R.id.alertTitle,
        acceptButtonId = android.R.id.button1,
        declineButtonId = android.R.id.button2
    )
) : KScreen<CoppaDialogScreen>(),
    HasTitleDialogScreen by dialogScreen,
    AcceptableDialogScreen by dialogScreen,
    DeclinableDialogScreen by dialogScreen {

    override val layoutId: Int? = null

    override val viewClass: Class<*> = CoppaDialogFragment::class.java
}
