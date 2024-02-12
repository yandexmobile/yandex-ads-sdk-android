package com.yandex.ads.sample.shared_steps

import androidx.annotation.StringRes
import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.yandex.ads.sample.R
import com.yandex.ads.sample.pageobjects.HomeScreen
import com.yandex.ads.sample.pageobjects.clickNavigationItem
import io.github.kakaocup.kakao.common.utilities.getResourceString
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen

internal class GoToSection<ScenarioData>(
    private val navigationItem: NavigationItem
) : BaseScenario<ScenarioData>() {

    override val steps: TestContext<ScenarioData>.() -> Unit = {
        step("Перейти в раздел ${getResourceString(navigationItem.titleRes)}") {
            onScreen<HomeScreen> { clickNavigationItem(navigationItem.navigationItemClazz) }
        }
    }

    enum class NavigationItem {
        NATIVE_TEMPLATE,
        CUSTOM_NATIVE,
        POLICIES,
        APP_OPEN_AD,
        REWARDED,
        INLINE_BANNER,
        INTERSTITIAL,
        STICKY_BANNER,
        INSTREAM_IN_ROLL,
        INSTREAM_BINDER;
    }

    private companion object {

        val NavigationItem.titleRes: Int
            @StringRes
            get() = when (this) {
                NavigationItem.NATIVE_TEMPLATE -> R.string.native_template_title
                NavigationItem.CUSTOM_NATIVE -> R.string.native_custom_title
                NavigationItem.POLICIES -> R.string.policies
                NavigationItem.APP_OPEN_AD -> R.string.appopenad_title
                NavigationItem.REWARDED -> R.string.rewarded_title
                NavigationItem.INLINE_BANNER -> R.string.inline_banner_title
                NavigationItem.INTERSTITIAL -> R.string.interstitial_title
                NavigationItem.STICKY_BANNER -> R.string.sticky_banner_title
                NavigationItem.INSTREAM_IN_ROLL -> R.string.instream_inroll_title
                NavigationItem.INSTREAM_BINDER -> R.string.instream_binder_title
            }

        val NavigationItem.navigationItemClazz: Class<out HomeScreen.NavigationItem<*>>
            get() = when (this) {
                NavigationItem.NATIVE_TEMPLATE -> HomeScreen.NavigationItem.NativeTemplate::class.java
                NavigationItem.CUSTOM_NATIVE -> HomeScreen.NavigationItem.CustomNative::class.java
                NavigationItem.POLICIES -> HomeScreen.NavigationItem.Policies::class.java
                NavigationItem.APP_OPEN_AD -> HomeScreen.NavigationItem.AppOpenAd::class.java
                NavigationItem.REWARDED -> HomeScreen.NavigationItem.Rewarded::class.java
                NavigationItem.INLINE_BANNER -> HomeScreen.NavigationItem.InlineBanner::class.java
                NavigationItem.INTERSTITIAL -> HomeScreen.NavigationItem.Interstitial::class.java
                NavigationItem.STICKY_BANNER -> HomeScreen.NavigationItem.StickyBanner::class.java
                NavigationItem.INSTREAM_IN_ROLL -> HomeScreen.NavigationItem.InstreamInRoll::class.java
                NavigationItem.INSTREAM_BINDER -> HomeScreen.NavigationItem.InstreamBinder::class.java
            }
    }
}
