/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.mopub.interstitial

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import com.yandex.ads.sample.databinding.ActivityMopubInterstitialThirdpartyMediationBinding
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializer
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializerListener
import com.yandex.ads.sample.utils.Logger

class MoPubInterstitialThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMopubInterstitialThirdpartyMediationBinding
    private var interstitialAd: MoPubInterstitial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMopubInterstitialThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadInterstitialAd)
        interstitialAd = MoPubInterstitial(this, AD_UNIT_ID)
        configureInterstitialAd()
    }

    private fun configureInterstitialAd() {

        // Replace AD_UNIT_ID with your unique Ad Unit ID.
        interstitialAd?.interstitialAdListener = MoPubInterstitialAdEventListener()
    }

    private fun loadInterstitialAd(view: View) {
        showLoading()

        val listener = object : MoPubInitializerListener {
            override fun onMoPubSdkInitialized() {
                interstitialAd?.load()
            }
        }

        // Replace AD_UNIT_ID with your unique Ad Unit ID.
        MoPubInitializer.initializeSdk(this, AD_UNIT_ID, listener)
    }

    override fun onDestroy() {
        interstitialAd?.destroy()
        interstitialAd = null
        super.onDestroy()
    }

    private fun showLoading() {
        binding.apply {
            loadAdButton.isEnabled = false
            adLoadingProgress.isVisible = true
        }
    }

    private fun hideLoading() {
        binding.apply {
            adLoadingProgress.isVisible = false
            loadAdButton.isEnabled = true
        }
    }

    private inner class MoPubInterstitialAdEventListener : MoPubInterstitial.InterstitialAdListener {

        override fun onInterstitialLoaded(moPubInterstitial: MoPubInterstitial) {
            Logger.debug("onInterstitialLoaded")
            interstitialAd?.show()
            hideLoading()
        }

        override fun onInterstitialFailed(
            moPubInterstitial: MoPubInterstitial,
            errorCode: MoPubErrorCode
        ) {
            Logger.error("Error code: $errorCode")
            hideLoading()
        }

        override fun onInterstitialShown(interstitial: MoPubInterstitial) {
            Logger.debug("onInterstitialShown")
        }

        override fun onInterstitialClicked(interstitial: MoPubInterstitial) {
            Logger.debug("onInterstitialClicked")
        }

        override fun onInterstitialDismissed(interstitial: MoPubInterstitial) {
            Logger.debug("onInterstitialDismissed")
        }
    }

    private companion object {

        const val AD_UNIT_ID = "b45ae93f5308475384fe112242821180"
    }
}