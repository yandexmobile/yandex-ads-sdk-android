package com.yandex.ads.sample.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.shared_steps.CheckChangePolicyAfterDialogAction
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkChangePolicy
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import org.junit.Rule
import org.junit.Test

internal class LocationPolicyChangeTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun check() = run {
        openSampleApp()
        goToSection(GoToSection.NavigationItem.POLICIES)
        checkChangePolicy(
            itemType = CheckChangePolicyAfterDialogAction.ItemType.LOCATION,
            itemState = CheckChangePolicyAfterDialogAction.ItemState.DISABLED,
            dialogAction = CheckChangePolicyAfterDialogAction.DialogAction.ACCEPT,
            checkDialogOpening = true
        )
        checkChangePolicy(
            itemType = CheckChangePolicyAfterDialogAction.ItemType.LOCATION,
            itemState = CheckChangePolicyAfterDialogAction.ItemState.ENABLED,
            dialogAction = CheckChangePolicyAfterDialogAction.DialogAction.DECLINE,
            checkDialogOpening = false
        )
    }
}
