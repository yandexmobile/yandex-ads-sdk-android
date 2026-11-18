package com.yandex.ads.sample.pageobjects

import androidx.test.espresso.DataInteraction
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.InlineBannerAdActivity
import com.yandex.ads.sample.components.DefaultOrdinalSpinner
import com.yandex.ads.sample.components.LogsScrollView
import com.yandex.ads.sample.components.OrdinalItem
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal fun InlineBannerScreen.checkAdIsLoaded() = adView {
    isDisplayed()
}

internal fun InlineBannerScreen.clickAd() = adView {
    isVisible()
    click()
}

internal fun InlineBannerScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal class InlineBannerScreen : KScreen<InlineBannerScreen>(),
    HasNetworkMenu<InlineBannerScreen.NetworkItem> {

    override val layoutId: Int = R.layout.activity_inline_banner_ad

    override val viewClass = InlineBannerAdActivity::class.java

    val adView = KView { withId(R.id.banner) }

    override val networkMenu = DefaultOrdinalSpinner<NetworkItem>(
        builder = { withClassName(Matchers.containsString("EndCompoundLayout")) },
        itemTypeBuilder = {
            itemType(NetworkItem::Yandex)
            itemType(NetworkItem::AdFox)
        },
        positionProvider = { clazz ->
            when (clazz) {
                NetworkItem.Yandex::class.java -> 0
                NetworkItem.AdFox::class.java -> 14
                else -> error("unsupported type")
            }
        }
    )

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val banner = KView { withId(R.id.banner) }

    val scrollView = LogsScrollView(
        parent = ViewBuilder()
            .apply { withId(R.id.ad_info) }
            .getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )

    sealed class NetworkItem(
        interaction: DataInteraction
    ) : OrdinalItem(interaction) {

        class Yandex(interaction: DataInteraction) : NetworkItem(interaction)

        class AdFox(interaction: DataInteraction) : NetworkItem(interaction)
    }
}
