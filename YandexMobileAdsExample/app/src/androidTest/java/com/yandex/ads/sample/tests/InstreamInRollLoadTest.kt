package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.InstreamInRollScreen
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class InstreamInRollLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldLoadInstreamInRollAd() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.INSTREAM_IN_ROLL)

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<InstreamInRollScreen> {
                clickLoadAd()
            }
        }

        step("Появилось сообщение о загрузке рекламы") {
            onScreen<InstreamInRollScreen> {
                flakySafely(60_000) {
                    this.logsView.apply {
                        containsMessage("Instream ad loaded")
                    }
                }
            }
        }
    }
}
