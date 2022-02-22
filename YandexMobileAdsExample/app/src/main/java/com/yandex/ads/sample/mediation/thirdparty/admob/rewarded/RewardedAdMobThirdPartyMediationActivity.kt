/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.admob.rewarded

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.yandex.ads.sample.databinding.ActivityAdmobInterstitialThirdpartyMediationBinding
import com.yandex.ads.sample.utils.Logger

class RewardedAdMobThirdPartyMediationActivity : AppCompatActivity() {

    private val rewardedAdListener = RewardedAdCallback()
    private lateinit var binding: ActivityAdmobInterstitialThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobInterstitialThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadRewardedAd)
    }

    private fun loadRewardedAd(view: View) {
        showLoading()

        // Replace demo Ad unit ID with actual Ad unit ID
        RewardedAd.load(this, AD_UNIT_ID, AdRequest.Builder().build(), rewardedAdListener)
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

    private inner class RewardedAdCallback: RewardedAdLoadCallback(), OnUserEarnedRewardListener {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            super.onAdLoaded(rewardedAd)
            Logger.debug("onAdLoaded")
            rewardedAd.show(this@RewardedAdMobThirdPartyMediationActivity, this)
            hideLoading()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            Logger.error(loadAdError.message)
            hideLoading()
        }

        override fun onUserEarnedReward(rewardItem: RewardItem) {
            val rewardedCallbackMessage = "onRewarded(), ${rewardItem.amount} ${rewardItem.type}"
            Logger.debug(rewardedCallbackMessage)
        }
    }

    private companion object {

        const val AD_UNIT_ID = "ca-app-pub-4449457472880521/6465842756"
    }
}