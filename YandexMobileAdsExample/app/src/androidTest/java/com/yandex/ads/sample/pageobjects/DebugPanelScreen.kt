package com.yandex.ads.sample.pageobjects

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.swipeUp
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KTextView

internal fun DebugPanelScreen.checkApplicationInfoBlockExists() {
    applicationInfoSectionHeader { isDisplayed() }
    applicationIdTitle { isDisplayed() }
    appVersionTitle { isDisplayed() }
    systemTitle { isDisplayed() }
    apiLevelTitle { isDisplayed() }
}

internal fun DebugPanelScreen.checkSdkIntegrationBlockExists() {
    sdkIntegrationSectionHeader { isDisplayed() }
    sdkVersionTitle { isDisplayed() }
    sdkIntegrationStatusTitle { isDisplayed() }
}

internal fun DebugPanelScreen.checkUserPrivacyBlockExists() {
    userPrivacySectionHeader { isDisplayed() }
    ageRestrictedUserTitle { isDisplayed() }
    hasLocationConsentTitle { isDisplayed() }
    hasUserConsentTitle { isDisplayed() }
    tcfConsentTitle { isDisplayed() }
}

internal fun DebugPanelScreen.checkFeaturesBlockExists() {
    featuresSectionHeader { isDisplayed() }
    debugErrorIndicatorTitle { isDisplayed() }
}

internal class DebugPanelScreen : KScreen<DebugPanelScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    private val recyclerView = KView {
        isInstanceOf(RecyclerView::class.java)
    }

    val applicationInfoSectionHeader = KTextView { withText("Application Info") }
    val applicationIdTitle = KTextView { withText("Application ID") }
    val appVersionTitle = KTextView { withText("App Version") }
    val systemTitle = KTextView { withText("System") }
    val apiLevelTitle = KTextView { withText("API Level") }

    val sdkIntegrationSectionHeader = KTextView { withText("SDK Integration") }
    val sdkVersionTitle = KTextView { withText("SDK Version") }
    val sdkIntegrationStatusTitle = KTextView { withText("SDK Integration Status") }

    val userPrivacySectionHeader = KTextView { withText("User Privacy") }
    val ageRestrictedUserTitle = KTextView { withText("Age Restricted User") }
    val hasLocationConsentTitle = KTextView { withText("Has Location Consent") }
    val hasUserConsentTitle = KTextView { withText("Has User Consent") }
    val tcfConsentTitle = KTextView { withText("TCF Consent") }

    val featuresSectionHeader = KTextView { withText("Features") }
    val debugErrorIndicatorTitle = KTextView { withText("Debug Error Indicator") }

    fun scrollDown() {
        recyclerView.view.perform(swipeUp())
        Thread.sleep(300)
        recyclerView.view.perform(swipeUp())
        Thread.sleep(300)
    }
}

