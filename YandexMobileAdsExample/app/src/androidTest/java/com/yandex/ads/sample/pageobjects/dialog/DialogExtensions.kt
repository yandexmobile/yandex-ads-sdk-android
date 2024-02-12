package com.yandex.ads.sample.pageobjects.dialog

import androidx.annotation.StringRes

internal fun HasTitleDialogScreen.hasTitleText(@StringRes titleRes: Int) {
    title.isDisplayed()
    title.hasText(titleRes)
}

internal fun AcceptableDialogScreen.clickAccept() {
    acceptButton.isDisplayed()
    acceptButton.isClickable()
    acceptButton.click()
}

internal fun DeclinableDialogScreen.clickDecline() {
    declineButton.isDisplayed()
    declineButton.isClickable()
    declineButton.click()
}

internal fun NeutralableDialogScreen.clickNeutral() {
    neutralButton.isDisplayed()
    neutralButton.isClickable()
    neutralButton.click()
}
