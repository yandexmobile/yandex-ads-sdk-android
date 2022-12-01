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
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavigationItem(
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
    val activity: Class<out Activity>,
)
