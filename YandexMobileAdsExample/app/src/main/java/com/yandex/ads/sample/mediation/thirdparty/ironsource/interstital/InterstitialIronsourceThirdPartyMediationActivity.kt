/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.ironsource.interstital

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InterstitialListener
import com.yandex.ads.sample.databinding.ActivityIronsouceInterstitialThirdpartyMediationBinding
import com.yandex.ads.sample.utils.Logger

class InterstitialIronsourceThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIronsouceInterstitialThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIronsouceInterstitialThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadInterstitialAd)
        IntegrationHelper.validateIntegration(this)
        IronSource.init(this, APP_KEY, IronSource.AD_UNIT.INTERSTITIAL)
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

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }

    override fun onDestroy() {
        IronSource.setInterstitialListener(null)
        super.onDestroy()
    }

    private fun loadInterstitialAd(view: View) {
        showLoading()
        IronSource.setInterstitialListener(IronSourceInterstitialListener())
        IronSource.loadInterstitial()
    }

    private inner class IronSourceInterstitialListener : InterstitialListener {

        override fun onInterstitialAdReady() {
            Logger.debug("onInterstitialAdReady")
            hideLoading()
            if (IronSource.isInterstitialReady()) {
                IronSource.showInterstitial()
            } else {
                Logger.error("Ad is not ready")
            }
        }

        override fun onInterstitialAdLoadFailed(ironSourceError: IronSourceError) {
            hideLoading()
            val errorMessage =
                "onInterstitialAdLoadFailed() code = ${ironSourceError.errorCode}, message = ${ironSourceError.errorMessage}"
            Logger.error(errorMessage)
        }

        override fun onInterstitialAdOpened() {
            Logger.debug("onInterstitialAdOpened")
        }

        override fun onInterstitialAdClosed() {
            Logger.debug("onInterstitialAdClosed")
        }

        override fun onInterstitialAdShowSucceeded() {
            Logger.debug("onInterstitialAdShowSucceeded")
        }

        override fun onInterstitialAdShowFailed(ironSourceError: IronSourceError) {
            val errorMessage =
                "onInterstitialAdShowFailed() code = ${ironSourceError.errorCode}, message = ${ironSourceError.errorMessage}"
            Logger.error(errorMessage)
        }

        override fun onInterstitialAdClicked() {
            Logger.debug("onInterstitialAdClicked")
        }
    }

    private companion object {

        const val APP_KEY = "85460dcd"
    }
}