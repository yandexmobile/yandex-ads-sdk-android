package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.BuildConfig
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserOrStoreIsOpened
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import com.yandex.mobile.ads.common.AdActivity
import io.github.kakaocup.kakao.text.KButton
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class AppOpenAdLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.APP_OPEN_AD)

        step("Свернуть приложение") {
            device.uiDevice.pressHome()

            flakySafely {
                val currentPackageName = device.uiDevice.currentPackageName
                Assert.assertNotEquals("", currentPackageName, BuildConfig.APPLICATION_ID)
            }
        }

        step("Вернуться в приложение") {
            device.uiDevice.pressRecentApps()
            Thread.sleep(1000)
            device.uiDevice.pressRecentApps()

            flakySafely {
                val currentPackageName = device.uiDevice.currentPackageName
                Assert.assertEquals("", currentPackageName, BuildConfig.APPLICATION_ID)
            }
        }

        step("Успешно отобразилась реклама") {
            flakySafely {
                device.activities.isCurrent(AdActivity::class.java)
            }
        }

        step("Кликнуть по рекламе/нажать на кнопку \"Перейти\"/\"Установить\"/\"Подробнее\" или любая другая кнопка, призывающая перейти по рекламе") {
            KButton { withTag("call_to_action") }.invoke {
                isVisible()
                isClickable()
                click()
            }

            checkAnyBrowserOrStoreIsOpened("Выполнен корректный переход в браузер или на установку рекламируемого приложения")
        }
    }
}
