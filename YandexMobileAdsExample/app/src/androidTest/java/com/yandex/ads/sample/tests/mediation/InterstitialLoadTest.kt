package com.yandex.ads.sample.tests.mediation

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.base.currentIsExternal
import com.yandex.ads.sample.base.defaultExt
import com.yandex.ads.sample.pageobjects.InterstitialScreen
import com.yandex.ads.sample.pageobjects.SomeKindOfAppScreen
import com.yandex.ads.sample.pageobjects.clickShowAd
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
internal class InterstitialLoadTest(
    private val networkItem: Class<out InterstitialScreen.NetworkItem>
) : BaseUITest() {
    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.INTERSTITIAL)
        choiceNetwork(
            screen = InterstitialScreen::class.java,
            item = networkItem
        )

        val interstitialScreen = InterstitialScreen()
        step("Подождать результат загрузки рекламы") {
            compose(timeoutMs = 60_000) {
                or(interstitialScreen.showAdButton) {
                    isEnabled()
                }
                or(interstitialScreen.logsView) {
                    containsMessage(NO_ADS_AVAILABLE_MESSAGE)
                }
            }
        }

        step("Нажать на кнопку \"Show ad\"") {
            onScreen<InterstitialScreen> {
                clickShowAd()
            }

            step("Реклама загрузилась. В случае подбора рекламы отобразилась на полный экран.") {
                val adScreen = SomeKindOfAppScreen()
                compose(
                    timeoutMs = 30_000,
                    allowedExceptions = FlakySafetyParams.defaultExt
                ) {
                    or(adScreen.rootView) {
                        isDisplayed()
                        device.activities.currentIsExternal()
                    }

                    or(interstitialScreen.logsView) {
                        containsMessage(NO_ADS_AVAILABLE_MESSAGE)
                    }
                }
            }
        }
    }

    companion object {

        private const val NO_ADS_AVAILABLE_MESSAGE =
            "Ad request completed successfully, but there are no ads available."

        @JvmStatic
        @Parameterized.Parameters
        fun data() = arrayOf(
            InterstitialScreen.NetworkItem.Vungle::class.java,
            InterstitialScreen.NetworkItem.UnityAds::class.java,
            InterstitialScreen.NetworkItem.Tapjoy::class.java,
            InterstitialScreen.NetworkItem.StartApp::class.java,
            InterstitialScreen.NetworkItem.Pangle::class.java,
            InterstitialScreen.NetworkItem.MyTarget::class.java,
//            InterstitialScreen.NetworkItem.Mintegral::class.java,
            InterstitialScreen.NetworkItem.InMobi::class.java,
            InterstitialScreen.NetworkItem.Chartboost::class.java,
            InterstitialScreen.NetworkItem.AppLovin::class.java,
            InterstitialScreen.NetworkItem.AdMob::class.java,
            InterstitialScreen.NetworkItem.AdColony::class.java,
        )
    }
}
