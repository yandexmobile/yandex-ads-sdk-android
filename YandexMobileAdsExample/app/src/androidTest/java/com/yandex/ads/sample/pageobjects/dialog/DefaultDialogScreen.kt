package com.yandex.ads.sample.pageobjects.dialog

import androidx.core.content.res.ResourcesCompat
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

interface DialogScreen : AcceptableDialogScreen,
    DeclinableDialogScreen,
    NeutralableDialogScreen,
    HasTitleDialogScreen

internal class DefaultDialogScreen(
    acceptButtonBuilder: ViewBuilder.() -> Unit,
    declineButtonBuilder: ViewBuilder.() -> Unit,
    neutralButtonBuilder: ViewBuilder.() -> Unit,
    titleTextViewBuilder: ViewBuilder.() -> Unit
) : DialogScreen {

    constructor(
        acceptButtonId: Int = ResourcesCompat.ID_NULL,
        declineButtonId: Int = ResourcesCompat.ID_NULL,
        neutralButtonId: Int = ResourcesCompat.ID_NULL,
        titleTextViewId: Int = ResourcesCompat.ID_NULL
    ) : this(
        acceptButtonBuilder = { withId(acceptButtonId) },
        declineButtonBuilder = { withId(declineButtonId) },
        neutralButtonBuilder = { withId(neutralButtonId) },
        titleTextViewBuilder = { withId(titleTextViewId) }
    )

    override val title = KTextView(titleTextViewBuilder)

    override val acceptButton = KButton(acceptButtonBuilder)

    override val declineButton = KButton(declineButtonBuilder)

    override val neutralButton = KButton(neutralButtonBuilder)
}
