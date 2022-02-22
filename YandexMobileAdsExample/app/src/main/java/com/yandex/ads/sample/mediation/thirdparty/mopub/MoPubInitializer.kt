/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.mopub

import android.content.Context
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener

interface MoPubInitializerListener {
    fun onMoPubSdkInitialized()
}

object MoPubInitializer {

    fun initializeSdk(
        context: Context,
        adUnitId: String,
        moPubInitializerListener: MoPubInitializerListener
    ) {
        if (MoPub.isSdkInitialized()) {
            moPubInitializerListener.onMoPubSdkInitialized()
        } else {
            val sdkConfiguration = SdkConfiguration.Builder(adUnitId).build()
            val initializationListener =
                SdkInitializationListener { moPubInitializerListener.onMoPubSdkInitialized() }
            MoPub.initializeSdk(context, sdkConfiguration, initializationListener)
        }
    }
}