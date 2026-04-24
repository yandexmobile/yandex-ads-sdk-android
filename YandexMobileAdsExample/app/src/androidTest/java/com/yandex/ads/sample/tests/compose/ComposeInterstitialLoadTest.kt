package com.yandex.ads.sample.tests.compose

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.compose.ComposeExamplesActivity
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.clickCallToAction
import com.yandex.mobile.ads.common.AdActivity
import org.junit.Rule
import org.junit.Test

internal class ComposeInterstitialLoadTest : BaseUITest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComposeExamplesActivity>()

    @Test
    fun shouldLoadComposeInterstitialAdAndNavigateToBrowser() = run {
        step("Перейти на экран Compose Interstitial") {
            composeTestRule.onNodeWithText("Interstitial").performClick()
        }

        step("Нажать на кнопку \"Load\"") {
            composeTestRule.onNodeWithText("Load").performClick()
        }

        step("Подождать результат загрузки рекламы") {
            composeTestRule.waitUntil(timeoutMillis = 60_000) {
                composeTestRule
                    .onAllNodesWithText("onAdLoaded", substring = true)
                    .fetchSemanticsNodes()
                    .isNotEmpty() ||
                composeTestRule
                    .onAllNodesWithText(NO_ADS_AVAILABLE_MESSAGE, substring = true)
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            }
        }

        step("Нажать на кнопку \"Show\"") {
            flakySafely {
                composeTestRule.onNodeWithText("Show").performClick()
            }

            step("Успешно отобразилась реклама") {
                flakySafely(30_000) {
                    device.activities.isCurrent(AdActivity::class.java)
                }
            }
        }

        clickCallToAction()

        checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
    }

    private companion object {
        private const val NO_ADS_AVAILABLE_MESSAGE =
            "Ad request completed successfully, but there are no ads available."
    }
}
