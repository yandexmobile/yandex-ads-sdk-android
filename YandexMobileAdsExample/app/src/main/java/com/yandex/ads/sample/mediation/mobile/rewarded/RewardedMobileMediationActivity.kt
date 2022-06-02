/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.mobile.rewarded

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityRewardedMobileMediationBinding
import com.yandex.ads.sample.mediation.mobile.common.MediationNetwork
import com.yandex.ads.sample.mediation.mobile.common.MediationNetworkAdapter
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener

class RewardedMobileMediationActivity : AppCompatActivity() {

    private val rewardedAdListener = RewardedAdMobileMediationEventListener()

    private var rewardedAd: RewardedAd? = null
    private lateinit var binding: ActivityRewardedMobileMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardedMobileMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadAdButton.setOnClickListener(::loadRewardedAd)

        binding.mediationProvidersSpinner.adapter = MediationNetworkAdapter(mediationNetworks, this)
    }

    private fun loadRewardedAd(view: View) {
        showLoading()

        destroyRewardedAd()
        rewardedAd = RewardedAd(this)
        configureRewardedAd()

        rewardedAd?.loadAd(AdRequest.Builder().build())
    }

    private fun destroyRewardedAd() {
        rewardedAd?.destroy()
        rewardedAd = null
    }

    private fun configureRewardedAd() {
        val adUnitId = getadUnitId()

        rewardedAd?.apply {
            setAdUnitId(adUnitId)
            setRewardedAdEventListener(rewardedAdListener)
        }
    }

    private fun getadUnitId(): String {
        val position = binding.mediationProvidersSpinner.selectedItemPosition

        // Replace demo mediationNetworks with actual Ad Unit IDs
        return mediationNetworks[position].adUnitId
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
        destroyRewardedAd()
        super.onDestroy()
    }

    private inner class RewardedAdMobileMediationEventListener : RewardedAdEventListener {

        override fun onAdLoaded() {
            rewardedAd?.show()
            hideLoading()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
            hideLoading()
        }

        override fun onImpression(impressionData: ImpressionData?) {
            Logger.debug("onImpression")
        }

        override fun onRewarded(reward: Reward) {
            val message = "onRewarded, amount = ${reward.amount}, type = ${reward.type}"
            Logger.debug(message)
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

        val mediationNetworks = listOf(
            MediationNetwork(R.string.yandex_title, "R-M-338228-6"),
            MediationNetwork(R.string.adcolony_title, "R-M-344723-12"),
            MediationNetwork(R.string.admob_title, "R-M-338228-1"),
            MediationNetwork(R.string.applovin_title, "R-M-338228-34"),
            MediationNetwork(R.string.facebook_title, "R-M-338228-5"),
            MediationNetwork(R.string.chartboost_title, "R-M-344723-15"),
            MediationNetwork(R.string.my_target_title, "R-M-338228-3"),
            MediationNetwork(R.string.ironsource_title, "R-M-338228-37"),
            MediationNetwork(R.string.pangle_title, "R-M-344723-23"),
            MediationNetwork(R.string.startapp_title, "R-M-338228-30"),
            MediationNetwork(R.string.tapjoy_title, "R-M-344723-24"),
            MediationNetwork(R.string.unity_ads_title, "R-M-338228-27"),
            MediationNetwork(R.string.vungle_title, "R-M-344723-21"),
        )
    }
}
