package com.yandex.ads.sample.components

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.matchers.SpinnerPopupMatcher
import io.github.kakaocup.kakao.delegate.ViewInteractionDelegate
import io.github.kakaocup.kakao.list.KAdapterItem
import io.github.kakaocup.kakao.list.KAdapterItemTypeBuilder
import io.github.kakaocup.kakao.spinner.KSpinner
import io.github.kakaocup.kakao.spinner.SpinnerAdapterActions
import io.github.kakaocup.kakao.spinner.SpinnerAdapterAssertions
import org.hamcrest.Matchers

abstract class OrdinalItem(dataInteraction: DataInteraction) :
    KAdapterItem<OrdinalItem>(dataInteraction)

interface OrdinalSpinner<T : OrdinalItem> : SpinnerAdapterActions,
    SpinnerAdapterAssertions,
    BaseAssertions {

    operator fun invoke(function: OrdinalSpinner<T>.() -> Unit)


    fun <I : T> onItem(clazz: Class<I>, block: I.() -> Unit)
}

internal class DefaultOrdinalSpinner<T : OrdinalItem> private constructor(
    private val spinner: KSpinner,
    private val positionProvider: (Class<out OrdinalItem>) -> Int
) : OrdinalSpinner<T>,
    SpinnerAdapterActions by spinner,
    SpinnerAdapterAssertions by spinner,
    BaseAssertions by spinner {

    constructor(
        builder: ViewBuilder.() -> Unit,
        itemTypeBuilder: KAdapterItemTypeBuilder.() -> Unit,
        positionProvider: (Class<out OrdinalItem>) -> Int
    ) : this(
        KSpinner(builder, itemTypeBuilder),
        positionProvider
    )

    override var popupView: ViewInteractionDelegate? = spinner.popupView

    override val view: ViewInteractionDelegate = spinner.view

    override fun invoke(function: OrdinalSpinner<T>.() -> Unit) = function.invoke(this)

    override fun <I : T> onItem(clazz: Class<I>, block: I.() -> Unit) {
        val position: Int = positionProvider.invoke(clazz)

        val provideItem = spinner.itemTypes.getOrElse(clazz.kotlin) {
            throw IllegalStateException("${clazz.simpleName} did not register to Spinner")
        }.provideItem

        val interaction = Espresso.onData(Matchers.anything())
            .inRoot(SpinnerPopupMatcher())
            .atPosition(position)

        block(provideItem(interaction) as I)
    }
}
