package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.shared_steps.CheckChangePolicyAfterDialogAction.DialogAction
import com.yandex.ads.sample.shared_steps.CheckChangePolicyAfterDialogAction.ItemState
import com.yandex.ads.sample.shared_steps.CheckChangePolicyAfterDialogAction.ItemType
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkChangePolicy
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import org.junit.Rule
import org.junit.Test

internal class CoppaPoliciesChangeTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.POLICIES)
        checkChangePolicy(
            itemType = ItemType.COPPA,
            itemState = ItemState.DISABLED,
            dialogAction = DialogAction.DECLINE,
            checkDialogOpening = true
        )
        checkChangePolicy(
            itemType = ItemType.COPPA,
            itemState = ItemState.DISABLED,
            dialogAction = DialogAction.ACCEPT,
            checkDialogOpening = false
        )
    }
}
