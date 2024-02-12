package com.yandex.ads.sample.components

import android.view.View
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Root
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import io.github.kakaocup.kakao.common.actions.BaseActions
import io.github.kakaocup.kakao.common.actions.ScrollableActions
import io.github.kakaocup.kakao.common.actions.SwipeableActions
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.delegate.ViewInteractionDelegate
import io.github.kakaocup.kakao.intercept.Interceptable
import io.github.kakaocup.kakao.scroll.KScrollView
import org.hamcrest.Matcher
import org.hamcrest.Matchers

internal interface LogsScrollViewActions : ScrollableActions, SwipeableActions

internal interface LogsScrollViewAssertions : BaseAssertions {

    fun hasSize(count: Int)

    fun hasMessage(message: String)

    fun containsMessage(message: String)
}

class LogsScrollView private constructor(private val scrollView: KScrollView) :
    LogsScrollViewActions,
    LogsScrollViewAssertions,
    Interceptable<ViewInteraction, ViewAssertion, ViewAction> by scrollView,
    BaseActions by scrollView,
    ScrollableActions by scrollView,
    SwipeableActions by scrollView,
    BaseAssertions by scrollView {

    constructor(function: ViewBuilder.() -> Unit) : this(KScrollView(function))

    constructor(
        parent: Matcher<View>,
        function: ViewBuilder.() -> Unit
    ) : this(
        KScrollView(
            parent,
            function
        )
    )

    constructor(
        parent: DataInteraction,
        function: ViewBuilder.() -> Unit
    ) : this(KScrollView(parent, function))

    override val view: ViewInteractionDelegate = scrollView.view

    override var root: Matcher<Root> = scrollView.root

    override fun act(function: () -> ViewAction) {
        scrollView.act(function)
    }

    override fun click(location: GeneralLocation) {
        scrollView.click(location)
    }

    override fun doubleClick(location: GeneralLocation) {
        scrollView.doubleClick(location)
    }

    override fun longClick(location: GeneralLocation) {
        scrollView.longClick(location)
    }

    override fun onFailure(function: (error: Throwable, matcher: Matcher<View>) -> Unit) {
        scrollView.onFailure(function)
    }

    override fun pressImeAction() {
        scrollView.pressImeAction()
    }

    override fun repeatUntil(
        maxAttempts: Int,
        action: () -> ViewAction,
        matcher: ViewBuilder.() -> Unit
    ) {
        scrollView.repeatUntil(maxAttempts, action, matcher)
    }

    override fun scrollTo() {
        scrollView.scrollTo()
    }

    override fun hasSize(count: Int) {
        view.check(ViewAssertions.matches(ViewMatchers.hasChildCount(1)))
    }

    override fun hasMessage(message: String) {
        view.check(
            ViewAssertions.matches(
                childMatcherOf(Matchers.`is`(message))
            )
        )
    }

    override fun containsMessage(message: String) {
        view.check(
            ViewAssertions.matches(
                childMatcherOf(Matchers.containsString(message))
            )
        )
    }

    private companion object {

        fun childMatcherOf(matcher: Matcher<String>): Matcher<View> {
            return ViewMatchers.withChild(ViewMatchers.withText(matcher))
        }
    }
}
