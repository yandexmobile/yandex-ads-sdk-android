package com.yandex.ads.sample.pageobjects

import androidx.test.espresso.DataInteraction
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.NativeTemplateAdActivity
import com.yandex.ads.sample.components.DefaultOrdinalSpinner
import com.yandex.ads.sample.components.LogsScrollView
import com.yandex.ads.sample.components.OrdinalItem
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal fun NativeTemplateScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal fun NativeTemplateScreen.clickCallToAction() = callToActionButton {
    isVisible()
    isClickable()
    click()
}

internal fun NativeTemplateScreen.checkAdIsLoaded() = adView {
    isDisplayed()
}

internal class NativeTemplateScreen : KScreen<NativeTemplateScreen>(),
    HasNetworkMenu<NativeTemplateScreen.NetworkItem> {

    override val layoutId: Int = R.layout.activity_native_template_ad

    override val viewClass: Class<*> = NativeTemplateAdActivity::class.java

    override val networkMenu = DefaultOrdinalSpinner<NetworkItem>(
        builder = { withClassName(Matchers.containsString("EndCompoundLayout")) },
        itemTypeBuilder = {
            itemType(NetworkItem::Yandex)
            itemType(NetworkItem::AdMob)
            itemType(NetworkItem::AdFox)
            itemType(NetworkItem::StartApp)
            itemType(NetworkItem::MyTarget)
        },
        positionProvider = { clazz ->
            when (clazz) {
                NetworkItem.Yandex::class.java -> 0
                NetworkItem.AdMob::class.java -> 1
                NetworkItem.MyTarget::class.java -> 2
                NetworkItem.StartApp::class.java -> 3
                NetworkItem.AdFox::class.java -> 4
                else -> error("unsupported type")
            }
        }
    )

    val logsView = LogsScrollView(
        parent = ViewBuilder().apply { withId(R.id.ad_info) }.getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )

    val adView = KView { withId(R.id.native_banner) }

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val callToActionButton = KButton { withTag("yma_call_to_action") }


    sealed class NetworkItem(
        interaction: DataInteraction
    ) : OrdinalItem(interaction) {

        class Yandex(interaction: DataInteraction) : NetworkItem(interaction)

        class AdMob(interaction: DataInteraction) : NetworkItem(interaction)

        class AdFox(interaction: DataInteraction) : NetworkItem(interaction)

        class StartApp(interaction: DataInteraction) : NetworkItem(interaction)

        class MyTarget(interaction: DataInteraction) : NetworkItem(interaction)
    }
}
