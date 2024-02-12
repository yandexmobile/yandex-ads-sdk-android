package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.InterstitialScreen
import com.yandex.ads.sample.pageobjects.clickCallToAction
import com.yandex.ads.sample.pageobjects.clickShowAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.choiceNetwork
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import com.yandex.mobile.ads.common.AdActivity
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class InterstitialLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.INTERSTITIAL)
        choiceNetwork(
            screen = InterstitialScreen::class.java,
            item = InterstitialScreen.NetworkItem.Yandex::class.java
        )

        Thread.sleep(10_000)

        step("Нажать на кнопку \"Show ad\"") {
            onScreen<InterstitialScreen> {
                clickShowAd()
            }

            step("Успешно отобразилась реклама") {
                flakySafely(30_000) {
                    device.activities.isCurrent(AdActivity::class.java)
                }
            }
        }

        step("Кликнуть по рекламе/нажать на кнопку \"Перейти\"/\"Установить\"/\"Подробнее\"") {
            onScreen<InterstitialScreen> { clickCallToAction() }

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }
}
