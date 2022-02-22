package com.yandex.ads.sample.mediation.mobile.common

/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

import androidx.annotation.StringRes

data class MediationNetwork(@StringRes val name: Int, val adUnitId: String)