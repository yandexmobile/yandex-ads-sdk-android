/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.gdpr

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityGdprBinding
import com.yandex.ads.sample.gdpr.settings.SettingGdprActivity
import com.yandex.ads.sample.gdpr.settings.SettingGdprFragment
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration

class GdprActivity : AppCompatActivity() {

    private var nativeAdLoader: NativeAdLoader? = null
    private var preferences: SharedPreferences? = null

    private lateinit var binding: ActivityGdprBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGdprBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadAdButton.setOnClickListener {
            if (isDialogShown()) {
                loadNativeAd()
            } else {
                showDialog()
            }
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        createNativeAdLoader()
        setUserConsent()
        setGdprDialogResultListener()

    }

    private fun setGdprDialogResultListener() {
        supportFragmentManager
            .setFragmentResultListener(GdprDialogFragment.REQUEST_KEY, this) { _, _ ->
                setUserConsent()
                loadNativeAd()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.settings == item.itemId) {
            val intent = Intent(this, SettingGdprActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNativeAdLoader() {
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader?.setNativeAdLoadListener(NativeLoadListener())
    }

    private fun loadNativeAd() {
        showLoading()

        // Replace demo Ad Unit ID with actual Ad Unit ID
        val nativeAdRequestConfiguration = NativeAdRequestConfiguration.Builder(AD_UNIT_ID)
            .setShouldLoadImagesAutomatically(true)
            .build()
        nativeAdLoader?.loadAd(nativeAdRequestConfiguration)
    }

    private fun setUserConsent() {
        val userConsent =
            preferences?.getBoolean(SettingGdprFragment.USER_CONSENT_KEY, false) ?: false
        MobileAds.setUserConsent(userConsent)
    }

    private fun isDialogShown(): Boolean {
        return preferences?.getBoolean(SettingGdprFragment.DIALOG_SHOWN_KEY, false) ?: false
    }

    private fun showDialog() {
        val dialogFragment = GdprDialogFragment()
        dialogFragment.show(supportFragmentManager, DIALOG_TAG)
    }

    private fun showLoading() {
        binding.apply {
            nativeAdView.isVisible = false
            adLoadingProgress.isVisible = true
            loadAdButton.isEnabled = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            adLoadingProgress.isVisible = false
            loadAdButton.isEnabled = true
        }
    }

    private inner class NativeLoadListener : NativeAdLoadListener {

        override fun onAdLoaded(nativeAd: NativeAd) {
            Logger.debug( "onAdLoaded")
            bindNativeAd(nativeAd)
            hideLoading()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
            hideLoading()
        }

        private fun bindNativeAd(nativeAd: NativeAd) {
            binding.nativeAdView.apply {
                setAd(nativeAd)
                isVisible = true
            }
        }
    }

    private companion object {

        const val AD_UNIT_ID = "R-M-DEMO-native-i"
        const val DIALOG_TAG = "dialog"
    }
}