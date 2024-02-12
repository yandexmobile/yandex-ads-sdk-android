package com.yandex.ads.sample.tests.mediation

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.base.currentIsExternal
import com.yandex.ads.sample.base.defaultExt
import com.yandex.ads.sample.pageobjects.RewardedScreen
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
internal class RewardedLoadTest(
    private val networkItem: Class<out RewardedScreen.NetworkItem>
) : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.REWARDED)
        choiceNetwork(
            screen = RewardedScreen::class.java,
            item = networkItem
        )

        Thread.sleep(10_000)

        step("Нажать на кнопку \"Show ad\"") {
            onScreen<RewardedScreen> {
                clickShowAd()
            }

            step("Реклама загрузилась. В случае подбора рекламы отобразилась на полный экран.") {
                val rewardedScreen = RewardedScreen()
                val adScreen = SomeKindOfAppScreen()

                compose(
                    timeoutMs = 30_000,
                    allowedExceptions = FlakySafetyParams.defaultExt
                ) {
                    or(rewardedScreen.logsView) {
                        containsMessage("Ad request completed successfully, but there are no ads available.")
                    }

                    or(adScreen.rootView) {
                        device.activities.currentIsExternal()
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = arrayOf(
            RewardedScreen.NetworkItem.Vungle::class.java,
            RewardedScreen.NetworkItem.UnityAds::class.java,
            RewardedScreen.NetworkItem.Tapjoy::class.java,
            RewardedScreen.NetworkItem.StartApp::class.java,
            RewardedScreen.NetworkItem.Pangle::class.java,
            RewardedScreen.NetworkItem.MyTarget::class.java,
//            RewardedScreen.NetworkItem.Mintegral::class.java,
            RewardedScreen.NetworkItem.InMobi::class.java,
            RewardedScreen.NetworkItem.Chartboost::class.java,
            RewardedScreen.NetworkItem.AppLovin::class.java,
            RewardedScreen.NetworkItem.AdMob::class.java,
            RewardedScreen.NetworkItem.AdColony::class.java,
        )
    }
}
