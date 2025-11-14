package com.yandex.ads.sample.pageobjects

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adunits.FeedActivity
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matcher

internal fun FeedScreen.clickShowFeed() = showFeedButton {
    isVisible()
    isClickable()
    click()
}

internal fun FeedScreen.clickShowConcatFeed() = showConcatFeedButton {
    isVisible()
    isClickable()
    click()
}

internal fun FeedScreen.checkFeedIsDisplayed() = feedRecyclerView {
    isVisible()
}

internal fun FeedScreen.checkFeedHasItems(): Int {
    var itemCount = 0
    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Check feed has items"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            require(adapter != null) { "Адаптер не должен быть null" }

            val maxAttempts = 20
            var attempts = 0
            while (adapter.itemCount == 0 && attempts < maxAttempts) {
                Thread.sleep(500)
                attempts++
            }

            itemCount = adapter.itemCount
            require(itemCount > 0) { "В ленте должна быть хотя бы одна запись после ожидания ${maxAttempts * 500}мс" }
        }
    })
    return itemCount
}

internal fun FeedScreen.getItemCount(): Int {
    var itemCount = 0
    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Get item count"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            itemCount = adapter?.itemCount ?: 0
        }
    })
    return itemCount
}

internal fun FeedScreen.scrollFeed(positions: Int = 10) {
    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Scroll feed"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            if (adapter != null && adapter.itemCount > 0) {
                for (i in 0 until positions) {
                    val targetPosition = minOf(i + 3, adapter.itemCount - 1)
                    recyclerView.smoothScrollToPosition(targetPosition)
                    Thread.sleep(300)
                }
            }
        }
    })
}

internal fun FeedScreen.scrollToAd() {
    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Scroll to first ad item"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter

            if (adapter != null && adapter.itemCount > 0) {
                var found = false
                var attempts = 0
                val maxAttempts = adapter.itemCount

                while (!found && attempts < maxAttempts) {
                    recyclerView.smoothScrollToPosition(attempts)
                    Thread.sleep(500)

                    for (i in 0 until recyclerView.childCount) {
                        val child = recyclerView.getChildAt(i)
                        if (child != null && isAdView(child)) {
                            found = true
                            break
                        }
                    }

                    attempts++
                }
            }
        }

        private fun isAdView(view: View): Boolean {
            val className = view.javaClass.simpleName
            if (className.contains("FeedItemContainer") ||
                className.contains("ExtendedNativeAdView")) {
                return true
            }

            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    if (isAdView(view.getChildAt(i))) {
                        return true
                    }
                }
            }

            return false
        }
    })
}


/**
 * Выводит в консоль полную иерархию View элементов в Feed RecyclerView.
 *
 * Используется для отладки и анализа структуры рекламных карточек.
 *
 * Для каждого элемента выводит:
 * - Класс View
 * - ID элемента
 * - Флаг clickable (кликабельность)
 * - Флаг enabled (доступность)
 * - Флаг visible (видимость)
 *
 * Пример вывода:
 * ```
 * ============ RecyclerView Info ============
 * Adapter: FeedAdAdapter
 * Item count: 5
 * Child count: 1
 *
 * --- Child 0 ---
 * FeedItemContainerView [id=-1, clickable=false, enabled=true, visible=true]
 *   ExtendedNativeAdView [id=-1, clickable=false, enabled=true, visible=true]
 *     DivImageView [id=2, clickable=true, enabled=true, visible=true]
 * ```
 */
internal fun FeedScreen.dumpViewHierarchy() {
    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Dump view hierarchy"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            println("============ RecyclerView Info ============")
            println("Adapter: ${recyclerView.adapter?.javaClass?.simpleName}")
            println("Item count: ${recyclerView.adapter?.itemCount ?: 0}")
            println("Child count: ${recyclerView.childCount}")
            println()

            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                println("--- Child $i ---")
                dumpView(child, 0)
                println()
            }
        }

        private fun dumpView(view: View, depth: Int) {
            val indent = "  ".repeat(depth)
            println("$indent${view.javaClass.simpleName} [id=${view.id}, clickable=${view.isClickable}, enabled=${view.isEnabled}, visible=${view.visibility == View.VISIBLE}]")

            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    dumpView(view.getChildAt(i), depth + 1)
                }
            }
        }
    })
}

internal fun FeedScreen.clickFirstAdItem() {

    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Click on first clickable view in feed item"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val firstChild = recyclerView.getChildAt(0)

            if (firstChild != null) {
                val clickableView = findFirstClickableView(firstChild)
                if (clickableView != null) {
                    clickableView.performClick()
                } else {
                    firstChild.performClick()
                }
            }
        }

        private fun findFirstClickableView(view: View): View? {
            if (view.isClickable && view.visibility == View.VISIBLE && view.isEnabled) {
                return view
            }

            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    val clickable = findFirstClickableView(view.getChildAt(i))
                    if (clickable != null) {
                        return clickable
                    }
                }
            }

            return null
        }
    })
}

internal fun FeedScreen.clickVisibleAdItem() {
    Thread.sleep(1000)

    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Click on visible ad item"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView

            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                if (child != null && isAdView(child)) {
                    val clickableView = findFirstClickableView(child)
                    if (clickableView != null) {
                        clickableView.performClick()
                        return
                    } else {
                        child.performClick()
                        return
                    }
                }
            }
        }

        private fun isAdView(view: View): Boolean {
            val className = view.javaClass.simpleName
            if (className.contains("FeedItemContainer") ||
                className.contains("ExtendedNativeAdView")) {
                return true
            }

            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    if (isAdView(view.getChildAt(i))) {
                        return true
                    }
                }
            }

            return false
        }

        private fun findFirstClickableView(view: View): View? {
            if (view.isClickable && view.visibility == View.VISIBLE && view.isEnabled) {
                return view
            }

            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    val clickable = findFirstClickableView(view.getChildAt(i))
                    if (clickable != null) {
                        return clickable
                    }
                }
            }

            return null
        }
    })
}

internal class FeedScreen : KScreen<FeedScreen>() {

    override val layoutId: Int = R.layout.activity_feed

    override val viewClass = FeedActivity::class.java

    val showFeedButton = KButton { withId(R.id.show_ad_button) }

    val showConcatFeedButton = KButton { withId(R.id.show_concat_ad_button) }

    val feedRecyclerView = KRecyclerView(
        { withId(R.id.feed_recycler_view) },
        itemTypeBuilder = {
            itemType(::FeedItemView)
        }
    )

    class FeedItemView(parent: Matcher<View>) : KRecyclerItem<FeedItemView>(parent)
}

