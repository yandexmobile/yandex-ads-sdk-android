/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.mopub.rewarded

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.mopub.common.MoPubReward
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubRewardedAdListener
import com.mopub.mobileads.MoPubRewardedAds
import com.yandex.ads.sample.databinding.ActivityMopubRewardedThirdpartyMediationBinding
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializer
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializerListener
import com.yandex.ads.sample.utils.Logger

class MoPubRewardedThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMopubRewardedThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMopubRewardedThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadAdButton.setOnClickListener(::loadRewardedAd)
    }

    private fun loadRewardedAd(view: View) {
        showLoading()

        val listener = object : MoPubInitializerListener {
            override fun onMoPubSdkInitialized() {
                MoPubRewardedAds.setRewardedAdListener(MoPubRewardedAdEventListener())

                // Replace demo Ad unit ID with actual Ad unit ID
                MoPubRewardedAds.loadRewardedAd(AD_UNIT_ID)
            }
        }

        // Replace demo Ad unit ID with actual Ad unit ID
        MoPubInitializer.initializeSdk(this, AD_UNIT_ID, listener)
    }

    override fun onDestroy() {
        MoPubRewardedAds.setRewardedAdListener(null)
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

    private inner class MoPubRewardedAdEventListener : MoPubRewardedAdListener {

        override fun onRewardedAdLoadSuccess(adUnitId: String) {
            Logger.debug("onInterstitialDismissed")
            // Replace AD_UNIT_ID with your unique Ad Unit ID.
            MoPubRewardedAds.showRewardedAd(AD_UNIT_ID)
            hideLoading()
        }

        override fun onRewardedAdLoadFailure(adUnitId: String, errorCode: MoPubErrorCode) {
            Logger.error("Error code: $errorCode")
            hideLoading()
        }

        override fun onRewardedAdCompleted(adUnitIds: Set<String?>, reward: MoPubReward) {
            val message = "onRewardedVideoCompleted(), reward = ${reward.amount} ${reward.label}"
            Logger.debug(message)
        }

        override fun onRewardedAdShowError(adUnitId: String, errorCode: MoPubErrorCode) {
            Logger.error("Error code: $errorCode")
        }

        override fun onRewardedAdClicked(adUnitId: String) {
            Logger.debug("onRewardedAdClicked")
        }

        override fun onRewardedAdClosed(adUnitId: String) {
            Logger.debug("onRewardedAdClosed")
        }

        override fun onRewardedAdStarted(adUnitId: String) {
            Logger.debug("onRewardedAdStarted")
        }
    }

    private companion object {

        const val AD_UNIT_ID = "b1e61a7fa8394b1aa7df3d4ce2fef552"
    }
}