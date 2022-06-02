/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.ironsource.rewarded

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.RewardedVideoListener
import com.yandex.ads.sample.databinding.ActivityIronsouceRewardedThirdpartyMediationBinding
import com.yandex.ads.sample.utils.Logger

class RewardedIronsourceThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIronsouceRewardedThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIronsouceRewardedThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadRewardedAd)
        IntegrationHelper.validateIntegration(this)
        IronSource.init(this, APP_KEY, IronSource.AD_UNIT.REWARDED_VIDEO)
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
        IronSource.setRewardedVideoListener(null)
        super.onDestroy()
    }

    private fun loadRewardedAd(view: View) {
        showLoading()
        IronSource.setRewardedVideoListener(IronSourceRewardedListener())
        IronSource.loadRewardedVideo()
    }

    private inner class IronSourceRewardedListener : RewardedVideoListener {

        override fun onRewardedVideoAdOpened() {
            Logger.debug("onRewardedVideoAdOpened")
        }

        override fun onRewardedVideoAdClosed() {
            Logger.debug("onRewardedVideoAdClosed")
        }

        override fun onRewardedVideoAvailabilityChanged(videoAvailable: Boolean) {
            Logger.debug("onRewardedVideoAvailabilityChanged: $videoAvailable")
            if (videoAvailable && binding.adLoadingProgress.isVisible) {
                IronSource.showRewardedVideo()
                hideLoading()
            }
        }

        override fun onRewardedVideoAdStarted() {
            Logger.debug("onRewardedVideoAdStarted")
        }

        override fun onRewardedVideoAdEnded() {
            Logger.debug("onRewardedVideoAdEnded")
        }

        override fun onRewardedVideoAdRewarded(placement: Placement?) {
            Logger.debug("onRewardedVideoAdRewarded: ${placement?.rewardAmount}")
        }

        override fun onRewardedVideoAdShowFailed(error: IronSourceError?) {
            Logger.error("onRewardedVideoAdShowFailed: ${error?.errorMessage}")
            hideLoading()
        }

        override fun onRewardedVideoAdClicked(placement: Placement?) {
            Logger.debug("onRewardedVideoAdClicked: ${placement?.placementName}")
        }
    }

    private companion object {

        const val APP_KEY = "10f882205"
    }
}
