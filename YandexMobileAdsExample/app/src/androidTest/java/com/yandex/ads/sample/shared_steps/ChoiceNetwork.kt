package com.yandex.ads.sample.shared_steps

import com.kaspersky.kaspresso.screens.KScreen
import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.yandex.ads.sample.components.OrdinalItem
import com.yandex.ads.sample.pageobjects.HasNetworkMenu
import com.yandex.ads.sample.pageobjects.clickNetworkItem
import com.yandex.ads.sample.pageobjects.openNetworkMenu

internal class ChoiceNetwork<ScenarioData, Item, SuperItem, Screen>(
    private val screen: Class<Screen>,
    private val item: Class<Item>
) : BaseScenario<ScenarioData>() where Item : SuperItem,
                                       SuperItem : OrdinalItem,
                                       Screen : KScreen<Screen>,
                                       Screen : HasNetworkMenu<SuperItem> {

    override val steps: TestContext<ScenarioData>.() -> Unit = {
        step("Во вкладке \"Network\" выбрать ${item.simpleName}") {
            val screen = screen.newInstance()

            screen {
                openNetworkMenu()
                clickNetworkItem(item)
            }
        }
    }
}
