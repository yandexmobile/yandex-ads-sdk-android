package com.yandex.ads.sample.pageobjects

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.yandex.ads.sample.R
import com.yandex.ads.sample.settings.PoliciesActivity
import io.github.kakaocup.kakao.common.matchers.PositionMatcher
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

internal inline fun <reified T : PoliciesScreen.PolicyItem<T>> PoliciesScreen.clickItem() {
    clickItem(T::class.java)
}

internal fun <T : PoliciesScreen.PolicyItem<T>> PoliciesScreen.clickItem(clazz: Class<T>) {
    onItem(clazz) {
        dialogButton.isDisplayed()
        dialogButton.isClickable()
        dialogButton.click()
    }
}

internal inline fun <reified T : PoliciesScreen.PolicyItem<T>> PoliciesScreen.checkTitleText(
    titleRes: Int
) = checkTitleText(T::class.java, titleRes)

internal fun <T : PoliciesScreen.PolicyItem<T>> PoliciesScreen.checkTitleText(
    clazz: Class<T>,
    titleRes: Int
) {
    onItem(clazz) {
        title.isDisplayed()
        title.hasText(titleRes)
    }
}

internal class PoliciesScreen(
    private val itemPositionProvider: (Class<PolicyItem<*>>) -> Int = {
        when (it) {
            PolicyItem.Gdpr::class.java -> 0
            PolicyItem.Location::class.java -> 1
            PolicyItem.Coppa::class.java -> 2
            else -> error("unsupported policy item")
        }
    }
) : KScreen<PoliciesScreen>() {

    override val layoutId: Int = R.layout.activity_policies

    override val viewClass: Class<*> = PoliciesActivity::class.java

    val policies = KRecyclerView(
        builder = { withId(R.id.policies) },
        itemTypeBuilder = {
            itemType { PolicyItem.Coppa(it) }
            itemType { PolicyItem.Location(it) }
            itemType { PolicyItem.Gdpr(it) }
        }
    )

    fun <T : PolicyItem<T>> onItem(clazz: Class<T>, block: PolicyItem<T>.() -> Unit) {
        val position = itemPositionProvider.invoke(clazz as Class<PolicyItem<*>>)

        val provideItem = policies.itemTypes.getOrElse(clazz.kotlin) {
            throw IllegalStateException("${clazz.simpleName} did not register to KRecyclerView")
        }.provideItem

        try {
            policies.scrollTo(position)
        } catch (_: Throwable) {
        }

        block((provideItem(PositionMatcher(policies.matcher, position)) as T)
            .also { policies.inRoot { withMatcher(this@PoliciesScreen.policies.root) } })
    }

    sealed class PolicyItem<T : PolicyItem<T>>(
        matcher: Matcher<View>
    ) : KRecyclerItem<T>(matcher) {

        val icon = KImageView(matcher) { withId(R.id.icon) }
        val title = KTextView(matcher) { withId(R.id.title) }
        val dialogButton = KButton(matcher) { withId(R.id.dialog_button) }

        class Coppa(matcher: Matcher<View>) : PolicyItem<Coppa>(matcher)

        class Location(matcher: Matcher<View>) : PolicyItem<Location>(matcher)

        class Gdpr(matcher: Matcher<View>) : PolicyItem<Gdpr>(matcher)
    }
}
