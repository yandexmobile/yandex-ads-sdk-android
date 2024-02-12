package com.yandex.ads.sample.pageobjects

import androidx.test.espresso.DataInteraction
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.CustomNativeAdActivity
import com.yandex.ads.sample.components.DefaultOrdinalSpinner
import com.yandex.ads.sample.components.LogsScrollView
import com.yandex.ads.sample.components.OrdinalItem
import com.yandex.ads.sample.components.OrdinalSpinner
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal fun CustomNativeScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal fun CustomNativeScreen.checkAdIsLoaded() = nativeAd {
    isDisplayed()
}

internal fun CustomNativeScreen.clickCallToAction() = callToAction {
    isVisible()
    isClickable()
    click()
}

internal class CustomNativeScreen : KScreen<CustomNativeScreen>(),
    HasNetworkMenu<CustomNativeScreen.NetworkItem> {

    override val layoutId: Int = R.layout.activity_custom_native_ad

    override val viewClass: Class<*> = CustomNativeAdActivity::class.java

    override val networkMenu: OrdinalSpinner<NetworkItem> = DefaultOrdinalSpinner(
        builder = { withClassName(Matchers.containsString("EndCompoundLayout")) },
        itemTypeBuilder = {
            itemType(NetworkItem::Yandex)
            itemType(NetworkItem::AdFox)
        },
        positionProvider = { clazz ->
            when (clazz) {
                NetworkItem.AdFox::class.java -> 1
                NetworkItem.Yandex::class.java -> 0
                else -> error("unsupported type")
            }
        }
    )

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val nativeAd = KView { withId(R.id.native_ad) }

    val callToAction = KView {
        withId(R.id.call_to_action)
    }

    val logsView = LogsScrollView(
        parent = ViewBuilder()
            .apply { withId(R.id.ad_info) }
            .getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )

    sealed class NetworkItem(dataInteraction: DataInteraction) : OrdinalItem(dataInteraction) {

        class Yandex(dataInteraction: DataInteraction) : NetworkItem(dataInteraction)

        class AdFox(dataInteraction: DataInteraction) : NetworkItem(dataInteraction)
    }
}
