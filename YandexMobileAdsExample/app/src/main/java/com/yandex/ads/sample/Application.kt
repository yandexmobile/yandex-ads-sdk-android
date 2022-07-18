/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample

import androidx.multidex.MultiDexApplication
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.instream.MobileInstreamAds

class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Logger.initialize(this)
        MobileInstreamAds.setAdGroupPreloading(INSTREAM_AD_GROUP_PRELOADING_ENABLED)
        MobileAds.initialize(this) {
            Logger.debug("SDK initialized")
        }
    }

    private companion object {
        private const val INSTREAM_AD_GROUP_PRELOADING_ENABLED = true
    }
}
