package com.yandex.ads.sample.tests.mediation

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.NativeTemplateScreen
import com.yandex.ads.sample.pageobjects.clickLoadAd
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.choiceNetwork
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class NativeTemplateLoadTest(
    private val networkItem: Class<out NativeTemplateScreen.NetworkItem>
) : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.NATIVE_TEMPLATE)
        choiceNetwork(
            screen = NativeTemplateScreen::class.java,
            item = networkItem
        )

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<NativeTemplateScreen> {
                clickLoadAd()
            }
        }

        step("Реклама загрузилась. В случае подбора рекламы она отображется") {
            onScreen<NativeTemplateScreen> {
                compose(timeoutMs = 60_000) {
                    or(adView) {
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

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = arrayOf(
            NativeTemplateScreen.NetworkItem.AdMob::class.java,
            NativeTemplateScreen.NetworkItem.AdFox::class.java,
            NativeTemplateScreen.NetworkItem.StartApp::class.java,
            NativeTemplateScreen.NetworkItem.MyTarget::class.java
        )
    }
}
