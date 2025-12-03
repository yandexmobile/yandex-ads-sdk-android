package com.yandex.ads.sample.pageobjects

import android.util.Log
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

internal fun FeedScreen.waitForAdToLoad(timeoutMs: Long = 60_000): Boolean {
    var adFound = false

    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Wait for ad to load"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val startTime = System.currentTimeMillis()
            val checkInterval = 1000L

            Log.d("KASPRESSO", "waitForAdToLoad: начинаем ожидание загрузки рекламы, таймаут ${timeoutMs}мс")

            while (System.currentTimeMillis() - startTime < timeoutMs) {
                var hasLoadedAd = false

                for (i in 0 until recyclerView.childCount) {
                    val child = recyclerView.getChildAt(i)
                    if (child != null && isAdView(child)) {
                        val hasClickableElement = findFirstClickableView(child) != null
                        Log.d("KASPRESSO", "waitForAdToLoad: найдена реклама на позиции $i, кликабельный элемент: $hasClickableElement")

                        if (hasClickableElement) {
                            hasLoadedAd = true
                            break
                        }
                    }
                }

                if (hasLoadedAd) {
                    Log.d("KASPRESSO", "waitForAdToLoad: реклама успешно загружена за ${System.currentTimeMillis() - startTime}мс")
                    adFound = true
                    return
                }

                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed % 10000 < checkInterval) {
                    Log.d("KASPRESSO", "waitForAdToLoad: реклама еще не загрузилась, прошло ${elapsed}мс")
                }

                Thread.sleep(checkInterval)
            }

            Log.e("KASPRESSO", "waitForAdToLoad: таймаут ожидания загрузки рекламы (${timeoutMs}мс)")
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

    return adFound
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
                val maxAttempts = 50
                val waitBetweenScrolls = 1000L
                val waitForAdLoad = 2000L

                Log.d("KASPRESSO", "scrollToAd: начинаем поиск рекламы, элементов в адаптере: ${adapter.itemCount}")

                while (!found && attempts < maxAttempts) {
                    val scrollPosition = minOf(attempts * 3, adapter.itemCount - 1)
                    Log.d("KASPRESSO", "scrollToAd: попытка $attempts, скроллим к позиции $scrollPosition")

                    recyclerView.smoothScrollToPosition(scrollPosition)
                    Thread.sleep(waitBetweenScrolls)

                    for (i in 0 until recyclerView.childCount) {
                        val child = recyclerView.getChildAt(i)
                        if (child != null && isAdView(child)) {
                            Log.d("KASPRESSO", "scrollToAd: найдена реклама на позиции $i, ждем загрузки ${waitForAdLoad}мс")
                            Thread.sleep(waitForAdLoad)
                            found = true
                            break
                        }
                    }

                    attempts++

                    if (attempts % 5 == 0) {
                        Log.d("KASPRESSO", "scrollToAd: выполнено $attempts попыток, текущее количество элементов: ${adapter.itemCount}")
                    }
                }

                if (found) {
                    Log.d("KASPRESSO", "scrollToAd: реклама успешно найдена и загружена")
                } else {
                    Log.e("KASPRESSO", "scrollToAd: реклама не найдена после $attempts попыток")
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
            val maxAttempts = 30
            val waitBetweenAttempts = 1000L
            var clicked = false

            Log.d("KASPRESSO", "clickFirstAdItem: начинаем поиск первого элемента для клика")

            for (attempt in 0 until maxAttempts) {
                Log.d("KASPRESSO", "clickFirstAdItem: попытка ${attempt + 1}/$maxAttempts")

                val firstChild = recyclerView.getChildAt(0)
                if (firstChild != null) {
                    val clickableView = findFirstClickableView(firstChild)
                    if (clickableView != null) {
                        Log.d("KASPRESSO", "clickFirstAdItem: найден кликабельный элемент ${clickableView.javaClass.simpleName}, выполняем клик")
                        clickableView.performClick()
                        clicked = true
                        return
                    } else if (firstChild.isClickable) {
                        Log.d("KASPRESSO", "clickFirstAdItem: первый элемент кликабелен, выполняем клик")
                        firstChild.performClick()
                        clicked = true
                        return
                    } else {
                        Log.w("KASPRESSO", "clickFirstAdItem: первый элемент найден, но не найден кликабельный подэлемент")
                    }
                } else {
                    Log.w("KASPRESSO", "clickFirstAdItem: первый элемент не найден")
                }

                if (!clicked) {
                    Log.d("KASPRESSO", "clickFirstAdItem: кликабельный элемент не найден, ждем ${waitBetweenAttempts}мс")
                    Thread.sleep(waitBetweenAttempts)
                }
            }

            if (!clicked) {
                Log.e("KASPRESSO", "clickFirstAdItem: не удалось найти и кликнуть первый элемент после $maxAttempts попыток")
                throw IllegalStateException("Не удалось найти кликабельный первый элемент после $maxAttempts попыток")
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
    feedRecyclerView.view.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "Click on visible ad item"

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val maxAttempts = 30
            val waitBetweenAttempts = 1000L
            var clicked = false

            Log.d("KASPRESSO", "clickVisibleAdItem: начинаем поиск видимой рекламы для клика")

            for (attempt in 0 until maxAttempts) {
                Log.d("KASPRESSO", "clickVisibleAdItem: попытка ${attempt + 1}/$maxAttempts")

                for (i in 0 until recyclerView.childCount) {
                    val child = recyclerView.getChildAt(i)
                    if (child != null && isAdView(child)) {
                        Log.d("KASPRESSO", "clickVisibleAdItem: найден рекламный элемент на позиции $i")

                        val clickableView = findFirstClickableView(child)
                        if (clickableView != null) {
                            Log.d("KASPRESSO", "clickVisibleAdItem: найден кликабельный элемент ${clickableView.javaClass.simpleName}, выполняем клик")
                            clickableView.performClick()
                            clicked = true
                            return
                        } else if (child.isClickable) {
                            Log.d("KASPRESSO", "clickVisibleAdItem: родительский элемент кликабелен, выполняем клик")
                            child.performClick()
                            clicked = true
                            return
                        } else {
                            Log.w("KASPRESSO", "clickVisibleAdItem: найдена реклама, но не найден кликабельный элемент")
                        }
                    }
                }

                if (!clicked) {
                    Log.d("KASPRESSO", "clickVisibleAdItem: реклама не найдена, ждем ${waitBetweenAttempts}мс")
                    Thread.sleep(waitBetweenAttempts)
                }
            }

            if (!clicked) {
                Log.e("KASPRESSO", "clickVisibleAdItem: не удалось найти и кликнуть рекламу после $maxAttempts попыток")
                throw IllegalStateException("Не удалось найти кликабельную рекламу после $maxAttempts попыток")
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

