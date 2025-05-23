package com.yandex.ads.sample.pageobjects

import androidx.test.espresso.DataInteraction
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.StickyBannerAdActivity
import com.yandex.ads.sample.components.DefaultOrdinalSpinner
import com.yandex.ads.sample.components.LogsScrollView
import com.yandex.ads.sample.components.OrdinalItem
import com.yandex.ads.sample.components.OrdinalSpinner
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal fun StickyBannerScreen.checkAdIsLoaded() = banner {
    isDisplayed()
}

internal fun StickyBannerScreen.clickAd() = banner {
    isVisible()
    click()
}

internal fun StickyBannerScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal class StickyBannerScreen : KScreen<StickyBannerScreen>(),
    HasNetworkMenu<StickyBannerScreen.NetworkItem> {

    override val layoutId: Int = R.layout.activity_sticky_banner_ad

    override val viewClass = StickyBannerAdActivity::class.java

    override val networkMenu: OrdinalSpinner<NetworkItem> = DefaultOrdinalSpinner(
        builder = { withClassName(Matchers.containsString("EndCompoundLayout")) },
        itemTypeBuilder = {
            itemType(NetworkItem::Yandex)
            itemType(NetworkItem::AdFox)
            itemType(NetworkItem::Vungle)
            itemType(NetworkItem::UnityAds)
            itemType(NetworkItem::StartApp)
            itemType(NetworkItem::MyTarget)
            itemType(NetworkItem::Mintegral)
            itemType(NetworkItem::InMobi)
            itemType(NetworkItem::Chartboost)
            itemType(NetworkItem::AppLovin)
            itemType(NetworkItem::AdMob)
        },
        positionProvider = { clazz ->
            when (clazz) {
                NetworkItem.AdFox::class.java -> 10
                NetworkItem.Vungle::class.java -> 9
                NetworkItem.UnityAds::class.java -> 8
                NetworkItem.StartApp::class.java -> 7
                NetworkItem.MyTarget::class.java -> 6
                NetworkItem.Mintegral::class.java -> 5
                NetworkItem.InMobi::class.java -> 4
                NetworkItem.Chartboost::class.java -> 3
                NetworkItem.AppLovin::class.java -> 2
                NetworkItem.AdMob::class.java -> 1
                NetworkItem.Yandex::class.java -> 0
                else -> error("unsupported type")
            }
        }
    )

    val banner = KView { withId(R.id.banner) }

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val scrollView = LogsScrollView(
        parent = ViewBuilder()
            .apply { withId(R.id.ad_info) }
            .getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )

    sealed class NetworkItem(dataInteraction: DataInteraction) : OrdinalItem(dataInteraction) {

        class Yandex(dataInteraction: DataInteraction) : NetworkItem(dataInteraction)

        class AdFox(interaction: DataInteraction) : NetworkItem(interaction)

        class Vungle(interaction: DataInteraction) : NetworkItem(interaction)

        class UnityAds(interaction: DataInteraction) : NetworkItem(interaction)

        class StartApp(interaction: DataInteraction) : NetworkItem(interaction)

        class MyTarget(interaction: DataInteraction) : NetworkItem(interaction)

        class Mintegral(interaction: DataInteraction) : NetworkItem(interaction)

        class InMobi(interaction: DataInteraction) : NetworkItem(interaction)

        class Chartboost(interaction: DataInteraction) : NetworkItem(interaction)

        class AppLovin(interaction: DataInteraction) : NetworkItem(interaction)

        class AdMob(interaction: DataInteraction) : NetworkItem(interaction)
    }
}
