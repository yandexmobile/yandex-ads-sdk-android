package com.yandex.ads.sample.pageobjects

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.viewpager2.widget.ViewPager2
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.AdfoxCarouselAdActivity
import com.yandex.ads.sample.components.LogsScrollView
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matcher
import org.hamcrest.Matchers

import androidx.test.espresso.DataInteraction

private fun atPosition(matcher: Matcher<View>, position: Int): Matcher<View> {
    return object : org.hamcrest.BaseMatcher<View>() {
        private var currentPosition = 0

        override fun describeTo(description: org.hamcrest.Description?) {
            description?.appendText("at position $position")
        }

        override fun matches(item: Any?): Boolean {
            if (matcher.matches(item)) {
                if (currentPosition == position) {
                    currentPosition++
                    return true
                }
                currentPosition++
            }
            return false
        }
    }
}

internal fun AdfoxCarouselScreen.clickLoadAd() = loadAdButton {
    isVisible()
    isClickable()
    click()
}

internal fun AdfoxCarouselScreen.checkAdIsLoaded() = carouselViewPager {
    isDisplayed()
}

internal fun AdfoxCarouselScreen.clickMedia() {
    KView {
        withId(R.id.media)
        withMatcher(ViewMatchers.isCompletelyDisplayed())
    }.click()
}

internal fun AdfoxCarouselScreen.clickTitle() {
    KView {
        withId(R.id.title)
        withMatcher(
            Matchers.allOf(
                ViewMatchers.isCompletelyDisplayed(),
                Matchers.not(ViewMatchers.withText("Logs"))
            )
        )
    }.click()
}

internal fun AdfoxCarouselScreen.clickFeedback() {
    Thread.sleep(300)
    try {
        Espresso.onView(
            atPosition(
                Matchers.allOf(
                    ViewMatchers.withId(R.id.feedback),
                    ViewMatchers.isCompletelyDisplayed()
                ),
                1
            )
        ).perform(ViewActions.click())
    } catch (e: Exception) {
        Espresso.onView(
            atPosition(
                Matchers.allOf(
                    ViewMatchers.withId(R.id.feedback),
                    ViewMatchers.isCompletelyDisplayed()
                ),
                0
            )
        ).perform(ViewActions.click())
    }
}

internal fun AdfoxCarouselScreen.checkLogsContain(vararg expectedTexts: String) {
    KView { withId(R.id.log) }.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Check logs contain expected texts"

        override fun perform(uiController: UiController, view: View) {
            val logTextView = view as? android.widget.TextView
            val logText = logTextView?.text?.toString() ?: ""
            expectedTexts.forEach { expectedText ->
                if (!logText.contains(expectedText, ignoreCase = true)) {
                    throw AssertionError("Ожидаемый текст '$expectedText' не найден в логах. Логи: $logText")
                }
            }
        }
    })
}

internal fun AdfoxCarouselScreen.clickLeftArrow() = leftArrowButton {
    isVisible()
    isClickable()
    click()
}

internal fun AdfoxCarouselScreen.clickRightArrow() = rightArrowButton {
    isVisible()
    isClickable()
    click()
}

internal fun AdfoxCarouselScreen.getCurrentPage(): Int {
    var currentPage = 0
    carouselViewPager.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = Matchers.instanceOf(ViewPager2::class.java)

        override fun getDescription(): String = "Get current page from ViewPager2"

        override fun perform(uiController: UiController, view: View) {
            currentPage = (view as ViewPager2).currentItem
        }
    })
    return currentPage
}

internal fun AdfoxCarouselScreen.getPageCount(): Int {
    var pageCount = 0
    carouselViewPager.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = Matchers.instanceOf(ViewPager2::class.java)

        override fun getDescription(): String = "Get page count from ViewPager2"

        override fun perform(uiController: UiController, view: View) {
            pageCount = (view as ViewPager2).adapter?.itemCount ?: 0
        }
    })
    return pageCount
}

internal fun AdfoxCarouselScreen.setCurrentPage(page: Int) {
    carouselViewPager.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = Matchers.instanceOf(ViewPager2::class.java)

        override fun getDescription(): String = "Set current page in ViewPager2"

        override fun perform(uiController: UiController, view: View) {
            (view as ViewPager2).setCurrentItem(page, true)
        }
    })
}

internal class AdfoxCarouselScreen : KScreen<AdfoxCarouselScreen>() {

    override val layoutId: Int = R.layout.activity_adfox_carousel_ad

    override val viewClass = AdfoxCarouselAdActivity::class.java

    val carouselViewPager = KView { withId(R.id.carousel_view_pager_2) }

    val loadAdButton = KButton { withId(R.id.load_ad_button) }

    val leftArrowButton = KView { withId(R.id.carousel_ad_left_button) }

    val rightArrowButton = KView { withId(R.id.carousel_ad_right_button) }

    val scrollView = LogsScrollView(
        parent = ViewBuilder()
            .apply { withId(R.id.ad_info) }
            .getViewMatcher(),
        function = { withClassName(Matchers.endsWith("ScrollView")) }
    )
}

