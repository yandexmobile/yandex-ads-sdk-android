package com.yandex.ads.sample.shared_steps

import androidx.test.uiautomator.UiSelector
import android.view.View
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
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

internal fun <ContextData> TestContext<ContextData>.clickShowAd() = scenarioOf<ContextData> {
    step("Нажать на кнопку \"Show ad\"") {
        flakySafely {
            device.uiDevice.findObject(
                UiSelector().resourceId("${BuildConfig.APPLICATION_ID}:id/show_ad_button")
            ).click()
        }
    }
}.let { scenario(it) }

internal fun <ContextData> TestContext<ContextData>.clickCallToAction() =
    scenarioOf<ContextData> {
        step("Кликнуть по кнопке призыва к действию") {
            flakySafely {
                val tags = listOf("yma_call_to_action", "call_to_action", "mac_call_to_action")
                var clicked = false
                InstrumentationRegistry.getInstrumentation().runOnMainSync {
                    val activities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED)
                    outer@ for (activity in activities) {
                        val root = activity.window.decorView
                        for (tag in tags) {
                            val view = root.findViewWithTag<View>(tag)
                            if (view != null && view.isShown) {
                                view.performClick()
                                clicked = true
                                break@outer
                            }
                        }
                    }
                }
                check(clicked) { "Call to action button not found with any of tags: $tags" }
            }
        }
    }.let { scenario(it) }

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
