package com.yandex.ads.sample.tests.mediation

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.CustomNativeScreen
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.choiceNetwork
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class CustomNativeLoadTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.CUSTOM_NATIVE)
        choiceNetwork(
            screen = CustomNativeScreen::class.java,
            item = CustomNativeScreen.NetworkItem.AdFox::class.java
        )

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<CustomNativeScreen> {
                clickLoadAd()
            }
        }

        step("Реклама загрузилась. В случае подбора рекламы она отображется") {
            onScreen<CustomNativeScreen> {
                compose(timeoutMs = 30_000) {
                    or(nativeAd) {
                        isCompletelyDisplayed()
                    }

                    or(logsView) {
                        hasSize(1)
                        containsMessage("Ad request completed successfully, but there are no ads available.")
                    }
                }
            }
        }
    }
}
