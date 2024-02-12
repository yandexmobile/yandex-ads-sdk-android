package com.yandex.ads.sample.shared_steps

import androidx.annotation.StringRes
import com.kaspersky.kaspresso.screens.KScreen
import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.yandex.ads.sample.R
import com.yandex.ads.sample.pageobjects.CoppaDialogScreen
import com.yandex.ads.sample.pageobjects.GdprDialogScreen
import com.yandex.ads.sample.pageobjects.LocationDialogScreen
import com.yandex.ads.sample.pageobjects.PoliciesScreen
import com.yandex.ads.sample.pageobjects.checkTitleText
import com.yandex.ads.sample.pageobjects.clickItem
import com.yandex.ads.sample.pageobjects.dialog.AcceptableDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.DeclinableDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.HasTitleDialogScreen
import com.yandex.ads.sample.pageobjects.dialog.clickAccept
import com.yandex.ads.sample.pageobjects.dialog.clickDecline
import com.yandex.ads.sample.pageobjects.dialog.hasTitleText
import io.github.kakaocup.kakao.common.utilities.getResourceString
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen

internal class CheckChangePolicyAfterDialogAction<ContextData>(
    private val itemType: ItemType,
    private val itemState: ItemState,
    private val dialogAction: DialogAction,
    private val checkDialogOpening: Boolean
) : BaseScenario<ContextData>() {

    override val steps: TestContext<ContextData>.() -> Unit = {
        val titleRes = when (itemState) {
            ItemState.ENABLED -> itemType.enabledTitleRes
            ItemState.DISABLED -> itemType.disabledTitleRes
        }

        val policyItemType = itemType.policyItemClazz
        val dialogType = itemType.createDialog()

        step("Возле пункта \"${getResourceString(titleRes)}\" нажать на кнопку \"Open dialog\"") {
            onScreen<PoliciesScreen> {
                checkTitleText(policyItemType, titleRes)
                clickItem(policyItemType)
            }

            if (checkDialogOpening) {
                step("Форма \"${getResourceString(itemType.dialogTitle)}\" открыта") {
                    dialogType {
                        dialogType.hasTitleText(itemType.dialogTitle)
                    }
                }
            }
        }

        val dialogButtonRes = when (dialogAction) {
            DialogAction.ACCEPT -> itemType.dialogAcceptButton
            DialogAction.DECLINE -> itemType.dialogDeclineButton
        }

        step("Нажать на кнопку \"${getResourceString(dialogButtonRes)}\"") {
            onScreen<CoppaDialogScreen> {
                when (dialogAction) {
                    DialogAction.ACCEPT -> clickAccept()
                    DialogAction.DECLINE -> clickDecline()
                }
            }

            val expectationTitle = when (itemState) {
                ItemState.ENABLED -> when (dialogAction) {
                    DialogAction.ACCEPT -> itemType.enabledTitleRes
                    DialogAction.DECLINE -> itemType.disabledTitleRes
                }

                ItemState.DISABLED -> when (dialogAction) {
                    DialogAction.ACCEPT -> itemType.enabledTitleRes
                    DialogAction.DECLINE -> itemType.disabledTitleRes
                }
            }

            val checkStepDescription = when (itemState) {
                ItemState.ENABLED -> when (dialogAction) {
                    DialogAction.ACCEPT -> "Пункт не сменил название на \"${getResourceString(itemType.disabledTitleRes)}\""
                    DialogAction.DECLINE -> "Пункт изменил название с \"${getResourceString(itemType.enabledTitleRes)}\" на \"${getResourceString(itemType.disabledTitleRes)}\""
                }

                ItemState.DISABLED -> when (dialogAction) {
                    DialogAction.ACCEPT -> "Пункт изменил название с \"${getResourceString(itemType.disabledTitleRes)}\" на \"${getResourceString(itemType.enabledTitleRes)}\""
                    DialogAction.DECLINE -> "Пункт не сменил название на \"${getResourceString(itemType.enabledTitleRes)}\""
                }
            }

            step(checkStepDescription) {
                onScreen<PoliciesScreen> { checkTitleText(policyItemType, expectationTitle) }
            }
        }
    }

    enum class ItemType {
        COPPA,
        GDPR,
        LOCATION;
    }

    enum class ItemState {
        ENABLED,
        DISABLED
    }

    enum class DialogAction {
        ACCEPT,
        DECLINE
    }

    private companion object {

        val ItemType.policyItemClazz: Class<out PoliciesScreen.PolicyItem<*>>
            get() = when (this) {
                ItemType.COPPA -> PoliciesScreen.PolicyItem.Coppa::class.java
                ItemType.GDPR -> PoliciesScreen.PolicyItem.Gdpr::class.java
                ItemType.LOCATION -> PoliciesScreen.PolicyItem.Location::class.java
            }

        fun <T> ItemType.createDialog(): T where
                T : KScreen<*>,
                T : HasTitleDialogScreen,
                T : AcceptableDialogScreen,
                T : DeclinableDialogScreen {
            return when (this) {
                ItemType.COPPA -> CoppaDialogScreen()
                ItemType.GDPR -> GdprDialogScreen()
                ItemType.LOCATION -> LocationDialogScreen()
            } as T
        }

        val ItemType.enabledTitleRes: Int
            @StringRes
            get() = when (this) {
                ItemType.COPPA -> R.string.coppa_enabled
                ItemType.GDPR -> R.string.gdpr_enabled
                ItemType.LOCATION -> R.string.location_enabled
            }

        val ItemType.disabledTitleRes: Int
            @StringRes
            get() = when (this) {
                ItemType.COPPA -> R.string.coppa_disabled
                ItemType.GDPR -> R.string.gdpr_disabled
                ItemType.LOCATION -> R.string.location_disabled
            }

        val ItemType.dialogTitle: Int
            @StringRes
            get() = when (this) {
                ItemType.COPPA -> R.string.coppa_dialog_title
                ItemType.GDPR -> R.string.gdpr_dialog_title
                ItemType.LOCATION -> R.string.location_dialog_title
            }

        val ItemType.dialogAcceptButton: Int
            @StringRes
            get() = when (this) {
                ItemType.GDPR -> R.string.coppa_underage
                ItemType.COPPA,
                ItemType.LOCATION -> R.string.accept
            }

        val ItemType.dialogDeclineButton: Int
            @StringRes
            get() = when (this) {
                ItemType.GDPR -> R.string.coppa__not_underage
                ItemType.COPPA,
                ItemType.LOCATION -> R.string.decline
            }
    }
}
