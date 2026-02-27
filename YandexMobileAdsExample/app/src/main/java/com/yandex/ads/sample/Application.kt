/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample

import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors
import com.yandex.ads.sample.appopenad.AppOpenAdManager
import com.yandex.ads.sample.settings.CoppaDialogFragment
import com.yandex.ads.sample.settings.GdprDialogFragment
import com.yandex.ads.sample.settings.LocationDialogFragment
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.YandexAds
import com.yandex.mobile.ads.instream.YandexInstreamAds

class Application : MultiDexApplication() {

    private val appOpenAdManager by lazy { AppOpenAdManager(this) }

    override fun onCreate() {
        super.onCreate()
        Logger.initialize(this)
        initPreferences()
        initSdk()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    private fun initSdk() {
        YandexInstreamAds.setAdGroupPreloading(INSTREAM_AD_GROUP_PRELOADING_ENABLED)
        YandexAds.initialize(this) {
            Logger.debug("SDK initialized")
            // wait until sdk initialized before using Ads
            initAppOpenAdManager()
        }
        YandexAds.enableLogging(true)
    }

    private fun initPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.run {
            YandexAds.privacy.setUserConsent(getBoolean(GdprDialogFragment.TAG, false))
            YandexAds.privacy.setLocationTracking(getBoolean(LocationDialogFragment.TAG, false))
            YandexAds.privacy.setAgeRestricted(getBoolean(CoppaDialogFragment.TAG, false))
        }
    }

    private fun initAppOpenAdManager() {
        appOpenAdManager.initialize()
    }

    private companion object {
        private const val INSTREAM_AD_GROUP_PRELOADING_ENABLED = true
    }
}
