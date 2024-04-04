/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yandex.mobile.ads.common.MobileAds

data class NavigationItem(
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
    val navigation: NavigationType,
) {

    fun navigate(context: Context) {
        when(navigation) {
            is NavigationType.ActivityNavigation -> {
                context.startActivity(Intent(context, navigation.activity))
            }

            is NavigationType.DebugPanelNavigation -> {
                MobileAds.showDebugPanel(context)
            }
        }
    }

    sealed interface NavigationType {

        data class ActivityNavigation(val activity: Class<out Activity>) : NavigationType

        object DebugPanelNavigation : NavigationType
    }
}
