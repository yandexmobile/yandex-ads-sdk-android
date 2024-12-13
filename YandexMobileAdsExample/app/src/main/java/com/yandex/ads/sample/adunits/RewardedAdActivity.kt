/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityRewardedAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.network.NetworkAdapter
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener

class RewardedAdActivity : AppCompatActivity(R.layout.activity_rewarded_ad),
    RewardedAdLoadListener {

    private val eventLogger = RewardedAdEventLogger()
    private var rewardedAdLoader: RewardedAdLoader? = null

    private var selectedIndex: Int = 0
    private val selectedNetwork get() = networks[selectedIndex]
    private var rewardedAd: RewardedAd? = null

    private lateinit var binding: ActivityRewardedAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardedAdBinding.inflate(layoutInflater)
        binding.setupUiBidding()
        setContentView(binding.root)

        // Rewarded ads loading should occur after initialization of the SDK.
        // Initialize SDK as early as possible, for example in Application.onCreate or at least Activity.onCreate
        // It's recommended to use the same instance of InterstitialAdLoader for every load for
        // achieve better performance
        rewardedAdLoader = RewardedAdLoader(this).apply {
            setAdLoadListener(this@RewardedAdActivity)
        }
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        disabeShowAdButton()
        rewardedAdLoader?.loadAd(createAdRequestConfiguration())
    }

    private fun createAdRequestConfiguration(): AdRequestConfiguration =
        AdRequestConfiguration.Builder(selectedNetwork.adUnitId).build()

    private fun showRewardedAd() {
        disabeShowAdButton()
        rewardedAd?.apply {
            setAdEventListener(eventLogger)
            show(this@RewardedAdActivity)
        }
    }

    override fun onAdLoaded(rewardedAd: RewardedAd) {
        log("Rewarded ad loaded")
        this.rewardedAd = rewardedAd
        enableShowAdButton()
    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        log(
            "Rewarded ad ${adRequestError.adUnitId} failed to load " +
                "with code ${adRequestError.code}: ${adRequestError.description}"
        )
        disabeShowAdButton()
    }

    private fun destroyRewardedAd() {
        // don't forget to clean up event listener to null?
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

    override fun onDestroy() {
        // Set listener to null to avoid memory leaks
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        destroyRewardedAd()
        super.onDestroy()
    }

    private inner class RewardedAdEventLogger : RewardedAdEventListener {

        override fun onAdShown() {
            log("Rewarded ad shown")
        }

        override fun onAdFailedToShow(adError: AdError) {
            log("Rewarded ad show error: $adError")
        }

        override fun onAdDismissed() {
            log("Rewarded ad dismissed")
            destroyRewardedAd()
            // Now you can preload the next interstitial ad.
            loadRewardedAd()
        }

        override fun onRewarded(reward: Reward) {
            log("Reward: ${reward.amount} of ${reward.type}")
        }

        override fun onAdClicked() {
            log("Rewarded ad clicked")
        }

        override fun onAdImpression(data: ImpressionData?) {
            log("Impression: ${data?.rawData}")
        }
    }

    private fun ActivityRewardedAdBinding.setupUiBidding() {
        showAdButton.setOnClickListener { showRewardedAd() }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        networkMenu.setStartIconDrawable(selectedNetwork.iconId)
        networkTextView.setText(selectedNetwork.titleId)
        networkTextView.setAdapter(NetworkAdapter(this@RewardedAdActivity, networks))
        networkTextView.setOnItemClickListener { _, _, position, _ ->
            selectedIndex = position
            networkMenu.setStartIconDrawable(selectedNetwork.iconId)
            loadRewardedAd()
        }
    }

    private fun enableShowAdButton() {
        binding.showAdButton.isEnabled = true
    }

    private fun disabeShowAdButton() {
        binding.showAdButton.isEnabled = false
    }

    private fun log(message: String) {
        binding.log.text = getString(R.string.log_format, binding.log.text, message)
    }

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-rewarded-yandex"),
            Network(R.drawable.ic_adcolony_icon_24, R.string.adcolony_title, "demo-rewarded-adcolony"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-rewarded-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-rewarded-applovin"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-rewarded-chartboost"),
            Network(R.drawable.ic_inmobi_icon, R.string.inmobi_title, "demo-rewarded-inmobi"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-rewarded-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-rewarded-mytarget"),
            Network(R.drawable.ic_ironsource_icon_24, R.string.ironsource_title, "demo-rewarded-ironsource"),
            Network(R.drawable.ic_pangle_icon, R.string.pangle_title, "demo-rewarded-pangle"),
            Network(R.drawable.ic_tapjoy_icon, R.string.tapjoy_title, "demo-rewarded-tapjoy"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-rewarded-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-rewarded-vungle"),
        )
    }
}
