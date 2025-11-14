package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.CustomNativeScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickCallToAction
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class CustomNativeLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldLoadCustomNativeAdAndNavigateToBrowser() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.CUSTOM_NATIVE)
        step("Нажать на кнопку \"Load ad\"") {
            onScreen<CustomNativeScreen> {
                clickLoadAd()
            }

            step("Баннер загрузился и отобразился без ошибок.") {
                onScreen<CustomNativeScreen> { checkAdIsLoaded() }
            }
        }
        step("Нажать на кнопку \"Перейти\"/\"Установить\"/\"Открыть\" или любая другая кнопка, призывающая к переходу по рекламе") {
            onScreen<CustomNativeScreen> { clickCallToAction() }

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }
}
