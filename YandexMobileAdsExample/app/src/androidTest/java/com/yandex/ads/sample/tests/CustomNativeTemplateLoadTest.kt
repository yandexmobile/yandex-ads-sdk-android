package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.NativeTemplateScreen
import com.yandex.ads.sample.pageobjects.checkAdIsLoaded
import com.yandex.ads.sample.pageobjects.clickCallToAction
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.choiceNetwork
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class CustomNativeTemplateLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldLoadNativeTemplateAndNavigateToBrowser() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.NATIVE_TEMPLATE)
        choiceNetwork(
            screen = NativeTemplateScreen::class.java,
            item = NativeTemplateScreen.NetworkItem.Yandex::class.java
        )
        step("Нажать на кнопку \"Load ad\"") {
            onScreen<NativeTemplateScreen> { clickLoadAd() }

            step("Баннер загрузился и отобразился без ошибок.") {
                onScreen<NativeTemplateScreen> { checkAdIsLoaded() }
            }
        }

        step("Кликнуть по рекламе/нажать на кнопку \"Перейти\"/\"Установить\" или любой другой кнопке, призывающей перейти по рекламе") {
            onScreen<NativeTemplateScreen> { clickCallToAction() }

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }
}
