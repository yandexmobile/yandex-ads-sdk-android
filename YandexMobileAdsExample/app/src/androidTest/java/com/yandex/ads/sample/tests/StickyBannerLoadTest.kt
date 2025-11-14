package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.StickyBannerScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickAd
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.choiceNetwork
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen
import org.junit.Rule
import org.junit.Test

internal class StickyBannerLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldLoadStickyBannerAndNavigateToBrowser() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.STICKY_BANNER)
        choiceNetwork(
            StickyBannerScreen::class.java,
            StickyBannerScreen.NetworkItem.Yandex::class.java,
        )

        step("Нажать на кнопку \"Load ad\"") {
            Screen.onScreen<StickyBannerScreen> {
                clickLoadAd()

                step("Баннер загрузился и отобразился корректно. Верстка не поехала.") {
                    checkAdIsLoaded()
                }
            }
        }

        step("Кликнуть по рекламе/нажать на кнопку \"Перейти\"/\"Установить\" или любой другой кнопке, призывающей перейти по рекламе") {
            Screen.onScreen<StickyBannerScreen> {
                clickAd()
            }

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }
}
