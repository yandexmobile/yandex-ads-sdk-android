package com.yandex.ads.sample.tests.compose

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.compose.ComposeExamplesActivity
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import org.junit.Rule
import org.junit.Test

internal class ComposeStickyBannerLoadTest : BaseUITest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComposeExamplesActivity>()

    @Test
    fun shouldLoadComposeStickyBannerAndNavigateToBrowser() = run {
        step("Перейти на экран Compose Sticky Banner") {
            composeTestRule.onNodeWithText("Sticky banner").performClick()
        }

        step("Дождаться загрузки баннера") {
            composeTestRule.waitUntil(timeoutMillis = 30_000) {
                composeTestRule
                    .onAllNodesWithText("onAdLoaded", substring = true)
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            }
        }

        step("Кликнуть по рекламе") {
            composeTestRule.onNodeWithTag(BANNER_TAG).performClick()

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }

    private companion object {
        private const val BANNER_TAG = "banner"
    }
}
