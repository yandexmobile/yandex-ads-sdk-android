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
        },
        positionProvider = { clazz ->
            when (clazz) {
                NetworkItem.Yandex::class.java -> 0
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
    }
}
