package com.yandex.ads.sample.pageobjects

import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.AppOpenAdActivity

internal class AppOpenAdScreen : KScreen<AppOpenAdScreen>() {

    override val layoutId: Int = R.layout.activity_app_open_ad

    override val viewClass: Class<*> = AppOpenAdActivity::class.java
}
