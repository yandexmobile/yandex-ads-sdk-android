package com.yandex.ads.sample.shared_steps

import com.kaspersky.kaspresso.screens.KScreen
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.yandex.ads.sample.BuildConfig
import com.yandex.ads.sample.components.OrdinalItem
import com.yandex.ads.sample.pageobjects.HasNetworkMenu
import org.junit.Assert

internal fun <ContextData> TestContext<ContextData>.openSampleApp() = scenarioOf<ContextData> {
    step("Войти в тестовое приложение сэмпла") {}
}.let { scenario(it) }

internal fun <ContextData> TestContext<ContextData>.goToSection(type: GoToSection.NavigationItem) {
    scenario(GoToSection(type))
}

internal fun <ContextData, Item, SuperItem, Screen> TestContext<ContextData>.choiceNetwork(
    screen: Class<Screen>,
    item: Class<Item>
) where Item : SuperItem,
        SuperItem : OrdinalItem,
        Screen : KScreen<Screen>,
        Screen : HasNetworkMenu<SuperItem> = scenario(
    ChoiceNetwork(
        screen,
        item
    )
)

internal fun <ContextData> TestContext<ContextData>.checkAnyBrowserIsOpened(
    description: String? = null
) = scenario(
    CheckThirdPartyAppIsOpened(
        apps = CheckThirdPartyAppIsOpened
            .ThirdPartyApp
            .Browsers
            .values()
            .toSet(),
        description = description
    )
)

internal fun <ContextData> TestContext<ContextData>.checkAnyBrowserOrStoreIsOpened(description: String? = null) {
    val apps = mutableSetOf<CheckThirdPartyAppIsOpened.ThirdPartyApp>()

    apps.addAll(CheckThirdPartyAppIsOpened.ThirdPartyApp.Browsers.values())
    apps.addAll(CheckThirdPartyAppIsOpened.ThirdPartyApp.Stores.values())

    scenario(
        CheckThirdPartyAppIsOpened(
            apps = apps,
            description = description
        )
    )
}

internal fun <ContextData> TestContext<ContextData>.returnToApplication() =
    scenarioOf<ContextData> {
        step("Вернуться в приложение") {
            device.uiDevice.pressBack()

            flakySafely {
                Assert.assertTrue(
                    "Не удалось вернуться в приложение по кнопке назад",
                    device.uiDevice.currentPackageName.contains(BuildConfig.APPLICATION_ID)
                )
            }
        }
    }.let { scenario(it) }

internal fun <ContextData> TestContext<ContextData>.checkChangePolicy(
    itemType: CheckChangePolicyAfterDialogAction.ItemType,
    itemState: CheckChangePolicyAfterDialogAction.ItemState,
    dialogAction: CheckChangePolicyAfterDialogAction.DialogAction,
    checkDialogOpening: Boolean = false
) {
    CheckChangePolicyAfterDialogAction<ContextData>(
        itemType = itemType,
        itemState = itemState,
        dialogAction = dialogAction,
        checkDialogOpening = checkDialogOpening
    ).let { scenario(it) }
}
