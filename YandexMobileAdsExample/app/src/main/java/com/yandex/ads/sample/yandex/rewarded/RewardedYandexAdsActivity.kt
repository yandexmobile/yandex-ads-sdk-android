/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.rewarded

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityRewardedYandexAdsBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener

class RewardedYandexAdsActivity : AppCompatActivity() {

    private var rewardedAd: RewardedAd? = null

    private var rewardedAdEventListener = RewardedAdYandexAdsEventListener()
    private lateinit var binding: ActivityRewardedYandexAdsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardedYandexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rewardedAd = RewardedAd(this)
        configureRewardedAd()
        binding.loadAdButton.setOnClickListener(::loadRewardedAd)
    }

    private fun loadRewardedAd(view: View) {
        showLoading()
        rewardedAd?.loadAd(AdRequest.Builder().build())
    }

    private fun configureRewardedAd() {

        // Replace demo Ad Unit ID with actual Ad Unit ID
        rewardedAd?.setAdUnitId(AD_UNIT_ID)
        rewardedAd?.setRewardedAdEventListener(rewardedAdEventListener)
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

    override fun onDestroy() {
        rewardedAd?.destroy()
        rewardedAd = null
        super.onDestroy()
    }

    private inner class RewardedAdYandexAdsEventListener : RewardedAdEventListener {

        override fun onAdLoaded() {
            Logger.debug("onAdLoaded")
            rewardedAd?.show()
            hideLoading()
        }

        override fun onRewarded(reward: Reward) {
            val message = "onRewarded, amount = ${reward.amount}, type = ${reward.type}"
            Logger.debug(message)
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            val message = "onAdFailedToLoad, error = ${adRequestError.description}"
            Logger.error(message)
            hideLoading()
        }

        override fun onImpression(impressionData: ImpressionData?) {
            Logger.debug("onImpression")
        }

        override fun onAdShown() {
            Logger.debug("onAdShown")
        }

        override fun onAdDismissed() {
            Logger.debug("onAdDismissed")
        }

        override fun onAdClicked() {
            Logger.debug( "onAdClicked")
        }

        override fun onLeftApplication() {
            Logger.debug("onLeftApplication")
        }

        override fun onReturnedToApplication() {
            Logger.debug("onReturnedToApplication")
        }
    }

    private companion object {

        const val AD_UNIT_ID = "R-M-DEMO-rewarded-client-side-rtb"
    }
}