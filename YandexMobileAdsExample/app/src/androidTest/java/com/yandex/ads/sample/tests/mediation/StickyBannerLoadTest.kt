package com.yandex.ads.sample.tests.mediation

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.StickyBannerScreen
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
internal class StickyBannerLoadTest(
    private val networkItem: Class<out StickyBannerScreen.NetworkItem>
) : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.STICKY_BANNER)
        choiceNetwork(
            screen = StickyBannerScreen::class.java,
            item = networkItem
        )

        step("Нажать на кнопку \"Load ad\"") {
            onScreen<StickyBannerScreen> {
                clickLoadAd()
            }
        }

        step("Баннер загрузился. В случае подбора рекламы отобразился") {
            onScreen<StickyBannerScreen> {
                compose(timeoutMs = 30_000) {
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
            StickyBannerScreen.NetworkItem.AdFox::class.java,
            StickyBannerScreen.NetworkItem.Vungle::class.java,
            StickyBannerScreen.NetworkItem.UnityAds::class.java,
            StickyBannerScreen.NetworkItem.StartApp::class.java,
            StickyBannerScreen.NetworkItem.MyTarget::class.java,
//            StickyBannerScreen.NetworkItem.Mintegral::class.java,
            StickyBannerScreen.NetworkItem.InMobi::class.java,
            StickyBannerScreen.NetworkItem.Chartboost::class.java,
            StickyBannerScreen.NetworkItem.AppLovin::class.java,
            StickyBannerScreen.NetworkItem.AdMob::class.java,
            StickyBannerScreen.NetworkItem.AdColony::class.java
        )
    }
}
