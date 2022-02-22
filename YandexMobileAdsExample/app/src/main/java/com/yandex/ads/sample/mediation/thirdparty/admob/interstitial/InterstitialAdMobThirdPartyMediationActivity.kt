/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.admob.interstitial

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.yandex.ads.sample.databinding.ActivityAdmobInterstitialThirdpartyMediationBinding
import com.yandex.ads.sample.utils.Logger

class InterstitialAdMobThirdPartyMediationActivity : AppCompatActivity() {

    private val interstitialAdListener = InterstitialAdThirdPartyLoadCallback()

    private lateinit var binding: ActivityAdmobInterstitialThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobInterstitialThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadInterstitialAd)
    }

    private fun loadInterstitialAd(view: View) {
        showLoading()

        // Replace demo Ad unit ID with actual Ad unit ID
        InterstitialAd.load(this, AD_UNIT_ID, AdRequest.Builder().build(), interstitialAdListener)
    }

    private fun showLoading() {
        binding.apply {
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

    private inner class InterstitialAdThirdPartyLoadCallback : InterstitialAdLoadCallback() {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            interstitialAd.show(this@InterstitialAdMobThirdPartyMediationActivity)
            Logger.debug("onInstreamAdPrepared")
            hideLoading()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            Logger.error(loadAdError.message)
            hideLoading()
        }
    }

    private companion object {

        const val AD_UNIT_ID = "ca-app-pub-4449457472880521/5005462050"
    }
}