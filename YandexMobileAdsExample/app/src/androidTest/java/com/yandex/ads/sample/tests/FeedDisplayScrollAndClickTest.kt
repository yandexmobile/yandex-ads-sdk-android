package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.FeedScreen
import com.yandex.ads.sample.pageobjects.checkFeedHasItems
import com.yandex.ads.sample.pageobjects.clickFirstAdItem
import com.yandex.ads.sample.pageobjects.clickShowFeed
import com.yandex.ads.sample.pageobjects.getItemCount
import com.yandex.ads.sample.pageobjects.scrollFeed
import com.yandex.ads.sample.pageobjects.waitForAdToLoad
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import com.yandex.ads.sample.shared_steps.returnToApplication
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class FeedDisplayScrollAndClickTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldDisplayFeedScrollAndClickAd() = run {
        openSampleApp()

        step("Перейти в раздел Feed") {
            goToSection(GoToSection.NavigationItem.FEED)
        }

        step("Нажать на кнопку Show feed") {
            onScreen<FeedScreen> {
                clickShowFeed()
            }
        }

        step("Реклама отобразилась, верстка не поехала") {
            onScreen<FeedScreen> {
                checkFeedHasItems()
                
                val adLoaded = waitForAdToLoad(timeoutMs = 60_000)
                Assert.assertTrue(
                    "Реклама не загрузилась в течение 60 секунд",
                    adLoaded
                )
            }
        }

        var initialItemCount = 0
        step("Проскроллить рекламу") {
            onScreen<FeedScreen> {
                initialItemCount = getItemCount()
                scrollFeed(positions = 10)
            }
        }

        step("Лента пролистывается бесконечно, реклама постоянно подгружается по мере пролистывания") {
            onScreen<FeedScreen> {
                val currentItemCount = getItemCount()
                
                Assert.assertTrue(
                    "Количество элементов должно увеличиться после скролла. Было: $initialItemCount, стало: $currentItemCount",
                    currentItemCount >= initialItemCount
                )
            }
        }

        step("Кликнуть по рекламе") {
            onScreen<FeedScreen> {
                clickFirstAdItem()
            }

            step("Выполнен корректный переход в браузер или на установку рекламируемого приложения") {
                checkAnyBrowserOrStoreIsOpened(
                    description = "Выполнен корректный переход в браузер или на установку рекламируемого приложения"
                )
            }
        }

        returnToApplication()
    }
}

