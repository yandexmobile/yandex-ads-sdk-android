package com.yandex.ads.sample.pageobjects

import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.InstreamAdBinderActivity
import com.yandex.ads.sample.components.LogsScrollView
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal fun InstreamBinderScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal class InstreamBinderScreen: KScreen<InstreamBinderScreen>() {

    override val layoutId: Int = R.layout.activity_instream_ad_binder

    override val viewClass = InstreamAdBinderActivity::class.java

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val logsView = LogsScrollView(
        parent = ViewBuilder().apply { withId(R.id.ad_info) }.getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )
}
