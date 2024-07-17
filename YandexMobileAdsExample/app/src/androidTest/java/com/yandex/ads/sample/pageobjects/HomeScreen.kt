package com.yandex.ads.sample.pageobjects

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.R
import io.github.kakaocup.kakao.common.matchers.PositionMatcher
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

internal inline fun <reified T : HomeScreen.NavigationItem<T>> HomeScreen.clickNavigationItem() =
    clickNavigationItem(T::class.java)

internal fun <T : HomeScreen.NavigationItem<T>> HomeScreen.clickNavigationItem(
    clazz: Class<T>
) = onItem(clazz) {
    isVisible()
    isClickable()
    click()
}

internal class HomeScreen(
    private val itemPositionProvider: (Class<NavigationItem<*>>) -> Int = { clazz ->
        when (clazz) {
            NavigationItem.StickyBanner::class.java -> 0
            NavigationItem.InlineBanner::class.java -> 1
            NavigationItem.Interstitial::class.java -> 2
            NavigationItem.Rewarded::class.java -> 3
            NavigationItem.NativeTemplate::class.java -> 4
            NavigationItem.CustomNative::class.java -> 5
            NavigationItem.AdfoxSlider::class.java -> 6
            NavigationItem.InstreamBinder::class.java -> 8
            NavigationItem.InstreamInRoll::class.java -> 9
            NavigationItem.Policies::class.java -> 10
            NavigationItem.AppOpenAd::class.java -> 11
            else -> error("unsupported type")
        }
    }
) : KScreen<HomeScreen>() {

    val adTypes = KRecyclerView(
        { withId(R.id.ad_types) },
        {
            itemType { NavigationItem.NativeTemplate(it) }
            itemType { NavigationItem.CustomNative(it) }
            itemType { NavigationItem.AdfoxSlider(it) }
            itemType { NavigationItem.Policies(it) }
            itemType { NavigationItem.AppOpenAd(it) }
            itemType { NavigationItem.Rewarded(it) }
            itemType { NavigationItem.InlineBanner(it) }
            itemType { NavigationItem.Interstitial(it) }
            itemType { NavigationItem.StickyBanner(it) }
            itemType { NavigationItem.InstreamInRoll(it) }
            itemType { NavigationItem.InstreamBinder(it) }
        }
    )

    override val layoutId: Int = R.layout.activity_home

    override val viewClass: Class<*> = HomeActivity::class.java

    fun <T : NavigationItem<T>> onItem(
        clazz: Class<T>,
        block: T.() -> Unit
    ) {
        val position = itemPositionProvider.invoke(clazz as Class<NavigationItem<*>>)

        val provideItem = adTypes.itemTypes.getOrElse(clazz.kotlin) {
            throw IllegalStateException("${clazz.simpleName} did not register to KRecyclerView")
        }.provideItem

        try {
            adTypes.scrollTo(position)
        } catch (_: Throwable) {
        }

        block((provideItem(PositionMatcher(adTypes.matcher, position)) as T)
            .also { adTypes.inRoot { withMatcher(this@HomeScreen.adTypes.root) } })
    }

    sealed class NavigationItem<T : NavigationItem<T>>(
        matcher: Matcher<View>
    ) : KRecyclerItem<T>(matcher) {

        class NativeTemplate(matcher: Matcher<View>) : NavigationItem<NativeTemplate>(matcher)

        class CustomNative(matcher: Matcher<View>) : NavigationItem<CustomNative>(matcher)

        class AdfoxSlider(matcher: Matcher<View>) : NavigationItem<AdfoxSlider>(matcher)

        class Policies(matcher: Matcher<View>) : NavigationItem<Policies>(matcher)

        class AppOpenAd(matcher: Matcher<View>) : NavigationItem<AppOpenAd>(matcher)

        class Rewarded(matcher: Matcher<View>) : NavigationItem<Rewarded>(matcher)

        class InlineBanner(matcher: Matcher<View>) : NavigationItem<InlineBanner>(matcher)

        class Interstitial(matcher: Matcher<View>) : NavigationItem<Interstitial>(matcher)

        class StickyBanner(matcher: Matcher<View>) : NavigationItem<StickyBanner>(matcher)

        class InstreamInRoll(matcher: Matcher<View>): NavigationItem<InstreamInRoll>(matcher)

        class InstreamBinder(matcher: Matcher<View>): NavigationItem<InstreamBinder>(matcher)
    }
}
