package com.yandex.ads.sample.pageobjects

import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.InstreamAdInrollActivity
import com.yandex.ads.sample.components.LogsScrollView
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal fun InstreamInRollScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal class InstreamInRollScreen : KScreen<InstreamInRollScreen>() {

    override val layoutId: Int = R.layout.activity_instream_ad_inroll

    override val viewClass = InstreamAdInrollActivity::class.java

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val logsView = LogsScrollView(
        parent = ViewBuilder().apply { withId(R.id.ad_info) }.getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )
}
