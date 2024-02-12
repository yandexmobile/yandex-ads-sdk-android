package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.InlineBannerScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickAd
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.choiceNetwork
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class InlineBannerLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.INLINE_BANNER)
        choiceNetwork(
            screen = InlineBannerScreen::class.java,
            item = InlineBannerScreen.NetworkItem.Yandex::class.java
        )

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<InlineBannerScreen> {
                clickLoadAd()

                step("Баннер загрузился и отобразился корректно. Верстка не поехала.") {
                    checkAdIsLoaded()
                }
            }
        }

        step("Кликнуть по рекламе") {
            onScreen<InlineBannerScreen> {
                clickAd()
            }

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }
}
