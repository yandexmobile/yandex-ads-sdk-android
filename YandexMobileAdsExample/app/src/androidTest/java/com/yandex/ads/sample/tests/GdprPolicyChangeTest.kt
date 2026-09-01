package com.yandex.ads.sample.tests

import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.test.ext.junit.rules.activityScenarioRule
import com.yandex.ads.sample.HomeActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.base.BaseUITest
import com.yandex.ads.sample.pageobjects.GdprDialogScreen
import com.yandex.ads.sample.pageobjects.PoliciesScreen
import com.yandex.ads.sample.pageobjects.clickItem
import com.yandex.ads.sample.pageobjects.dialog.clickNeutral
import com.yandex.ads.sample.pageobjects.dialog.hasTitleText
import com.yandex.ads.sample.shared_steps.CheckChangePolicyAfterDialogAction
import com.yandex.ads.sample.shared_steps.GoToSection
import com.yandex.ads.sample.shared_steps.checkAnyBrowserIsOpened
import com.yandex.ads.sample.shared_steps.checkChangePolicy
import com.yandex.ads.sample.shared_steps.goToSection
import com.yandex.ads.sample.shared_steps.openSampleApp
import com.yandex.ads.sample.shared_steps.returnToApplication
import com.yandex.ads.sample.settings.GdprDialogFragment
import com.yandex.mobile.ads.common.YandexAds
import io.github.kakaocup.kakao.common.utilities.getResourceString
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

internal class GdprPolicyChangeTest : BaseUITest() {

    @get:Rule
    val activityRule = activityScenarioRule<HomeActivity>()

    @Test
    fun shouldChangeGdprPolicyAndOpenPrivacyPage() = run {
        setGdprState(enabled = false)
        openSampleApp()
        goToSection(GoToSection.NavigationItem.POLICIES)
        step("Возле пункта \"${getResourceString(R.string.gdpr_disabled)}\" нажать на кнопку \"Open dialog\"") {
            onScreen<PoliciesScreen> { clickItem<PoliciesScreen.PolicyItem.Gdpr>() }

            step("Форма \"${getResourceString(R.string.gdpr_dialog_title)}\" открыта") {
                onScreen<GdprDialogScreen> { hasTitleText(R.string.gdpr_dialog_title) }
            }
        }
        step("Нажать на кнопку \"${getResourceString(R.string.about)}\"") {
            onScreen<GdprDialogScreen> { clickNeutral() }

            checkAnyBrowserIsOpened("Выполнен переход на страницу Privacy Policy")
        }
        returnToApplication()
        checkChangePolicy(
            itemType = CheckChangePolicyAfterDialogAction.ItemType.GDPR,
            itemState = CheckChangePolicyAfterDialogAction.ItemState.DISABLED,
            dialogAction = CheckChangePolicyAfterDialogAction.DialogAction.ACCEPT,
            checkDialogOpening = true
        )
    }

    @Test
    fun shouldDisableGdprPolicy() = run {
        setGdprState(enabled = true)
        openSampleApp()
        goToSection(GoToSection.NavigationItem.POLICIES)
        checkChangePolicy(
            itemType = CheckChangePolicyAfterDialogAction.ItemType.GDPR,
            itemState = CheckChangePolicyAfterDialogAction.ItemState.ENABLED,
            dialogAction = CheckChangePolicyAfterDialogAction.DialogAction.DECLINE,
            checkDialogOpening = true
        )
    }

    private fun setGdprState(enabled: Boolean) {
        activityRule.scenario.onActivity { activity ->
            PreferenceManager.getDefaultSharedPreferences(activity).edit {
                putBoolean(GdprDialogFragment.TAG, enabled)
            }
            YandexAds.setUserConsent(enabled)
        }
    }
}
