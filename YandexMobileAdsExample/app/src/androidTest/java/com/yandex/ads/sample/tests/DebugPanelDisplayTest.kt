package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.DebugPanelScreen
import com.yandex.ads.sample.pageobjects.checkApplicationInfoBlockExists
import com.yandex.ads.sample.pageobjects.checkFeaturesBlockExists
import com.yandex.ads.sample.pageobjects.checkSdkIntegrationBlockExists
import com.yandex.ads.sample.pageobjects.checkUserPrivacyBlockExists
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class DebugPanelDisplayTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldDisplayAllDebugPanelBlocks() = run {
        openSampleApp()

        step("Перейти в раздел Debug Panel") {
            goToSection(GoToSection.NavigationItem.DEBUG_PANEL)
        }

        step("Есть блок \"Информация о приложении\"") {
            onScreen<DebugPanelScreen> {
                flakySafely {
                    checkApplicationInfoBlockExists()
                }
            }
        }

        step("Есть блок \"Интеграция SDK\"") {
            onScreen<DebugPanelScreen> {
                flakySafely {
                    checkSdkIntegrationBlockExists()
                }
            }
        }

        step("Прокрутить вниз для проверки оставшихся блоков") {
            onScreen<DebugPanelScreen> {
                scrollDown()
            }
        }

        step("Есть блок \"Конфиденциальность пользователя\"") {
            onScreen<DebugPanelScreen> {
                flakySafely {
                    checkUserPrivacyBlockExists()
                }
            }
        }

        step("Есть блок \"Функции\"") {
            onScreen<DebugPanelScreen> {
                flakySafely {
                    checkFeaturesBlockExists()
                }
            }
        }
    }
}

