package com.yandex.ads.sample.pageobjects

import androidx.test.espresso.DataInteraction
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.components.DefaultOrdinalSpinner
import com.yandex.ads.sample.components.LogsScrollView
import com.yandex.ads.sample.components.OrdinalItem
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers

internal class RewardedScreen: KScreen<RewardedScreen>(),
    HasNetworkMenu<RewardedScreen.NetworkItem> {

    override val layoutId = R.layout.activity_rewarded_ad

    override val viewClass = RewardedScreen::class.java

    override val networkMenu = DefaultOrdinalSpinner<NetworkItem>(
        builder = { withClassName(Matchers.containsString("EndCompoundLayout")) },
        itemTypeBuilder = {
            itemType(NetworkItem::Yandex)
            itemType(NetworkItem::Vungle)
            itemType(NetworkItem::UnityAds)
            itemType(NetworkItem::StartApp)
            itemType(NetworkItem::MyTarget)
            itemType(NetworkItem::Mintegral)
            itemType(NetworkItem::InMobi)
            itemType(NetworkItem::Chartboost)
            itemType(NetworkItem::AppLovin)
            itemType(NetworkItem::AdMob)
            itemType(NetworkItem::Tapjoy)
            itemType(NetworkItem::Pangle)
        },
        positionProvider = { clazz ->
            when (clazz) {
                NetworkItem.Yandex::class.java -> 0
                NetworkItem.AdMob::class.java -> 2
                NetworkItem.AppLovin::class.java -> 3
                NetworkItem.Chartboost::class.java -> 6
                NetworkItem.InMobi::class.java -> 7
                NetworkItem.Mintegral::class.java -> 8
                NetworkItem.MyTarget::class.java -> 9
                NetworkItem.Pangle::class.java -> 11
                NetworkItem.StartApp::class.java -> 12
                NetworkItem.Tapjoy::class.java -> 13
                NetworkItem.UnityAds::class.java -> 14
                NetworkItem.Vungle::class.java -> 15
                else -> error("unsupported type")
            }
        }
    )

    val showAdButton = KButton { withId(R.id.show_ad_button) }

    val callToActionButton = KButton { withTag("call_to_action") }

    val logsView = LogsScrollView(
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )

    sealed class NetworkItem(
        interaction: DataInteraction
    ) : OrdinalItem(interaction) {

        class Yandex(interaction: DataInteraction) : NetworkItem(interaction)

        class Vungle(interaction: DataInteraction) : NetworkItem(interaction)

        class UnityAds(interaction: DataInteraction) : NetworkItem(interaction)

        class StartApp(interaction: DataInteraction) : NetworkItem(interaction)

        class MyTarget(interaction: DataInteraction) : NetworkItem(interaction)

        class Mintegral(interaction: DataInteraction) : NetworkItem(interaction)

        class InMobi(interaction: DataInteraction) : NetworkItem(interaction)

        class Chartboost(interaction: DataInteraction) : NetworkItem(interaction)

        class AppLovin(interaction: DataInteraction) : NetworkItem(interaction)

        class AdMob(interaction: DataInteraction) : NetworkItem(interaction)

        class Tapjoy(interaction: DataInteraction) : NetworkItem(interaction)

        class Pangle(interaction: DataInteraction) : NetworkItem(interaction)
    }
}
