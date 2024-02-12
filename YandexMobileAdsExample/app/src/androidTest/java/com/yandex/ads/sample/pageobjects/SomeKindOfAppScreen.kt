package com.yandex.ads.sample.pageobjects

import com.kaspersky.components.kautomator.component.common.views.UiView
import com.kaspersky.components.kautomator.screen.UiScreen
import com.yandex.ads.sample.BuildConfig

internal class SomeKindOfAppScreen : UiScreen<SomeKindOfAppScreen>() {

    override val packageName: String = BuildConfig.APPLICATION_ID

    val rootView = UiView {
        withIndex(0) { withPackage(this@SomeKindOfAppScreen.packageName) }
    }
}
