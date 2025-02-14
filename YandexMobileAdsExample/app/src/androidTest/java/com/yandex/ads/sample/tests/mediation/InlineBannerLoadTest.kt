package com.yandex.ads.sample.tests.mediation

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.InlineBannerScreen
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
internal class InlineBannerLoadTest(
    private val networkItem: Class<out InlineBannerScreen.NetworkItem>
) : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.INLINE_BANNER)
        choiceNetwork(
            screen = InlineBannerScreen::class.java,
            item = networkItem
        )

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<InlineBannerScreen> {
                clickLoadAd()
            }
        }

        step("Баннер загрузился. В случае подбора рекламы отобразился") {
            onScreen<InlineBannerScreen> {
                compose(timeoutMs = 60_000) {
                    or(banner) {
                        isCompletelyDisplayed()
                    }

                    or(scrollView) {
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
            InlineBannerScreen.NetworkItem.AdFox::class.java,
            InlineBannerScreen.NetworkItem.Vungle::class.java,
            InlineBannerScreen.NetworkItem.UnityAds::class.java,
            InlineBannerScreen.NetworkItem.StartApp::class.java,
//            InlineBannerScreen.NetworkItem.Mintegral::class.java,
//            InlineBannerScreen.NetworkItem.InMobi::class.java,
            InlineBannerScreen.NetworkItem.Chartboost::class.java,
            InlineBannerScreen.NetworkItem.AppLovin::class.java,
            InlineBannerScreen.NetworkItem.AdMob::class.java,
            InlineBannerScreen.NetworkItem.AdColony::class.java,
            InlineBannerScreen.NetworkItem.MyTarget::class.java
        )
    }
}
