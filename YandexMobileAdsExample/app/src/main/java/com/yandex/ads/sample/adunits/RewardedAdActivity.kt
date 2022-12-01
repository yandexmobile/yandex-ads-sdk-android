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
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityRewardedAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener

class RewardedAdActivity : AppCompatActivity(R.layout.activity_rewarded_ad) {

    private val adInfoFragment get() = _adInfoFragment!!
    private val eventLogger = RewardedAdEventLogger()

    private var adUnitId = ""
    private var _adInfoFragment: AdInfoFragment? = null
    private var rewardedAd: RewardedAd? = null

    private lateinit var binding: ActivityRewardedAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardedAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadRewarded
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
    }

    private fun loadRewarded() {
        if (adUnitId != adInfoFragment.selectedNetwork.adUnitId) {
            adUnitId = adInfoFragment.selectedNetwork.adUnitId
            destroyRewarded()
            createRewarded()
        }
        rewardedAd?.loadAd(AdRequest.Builder().build())
    }

    private fun createRewarded() {
        rewardedAd = RewardedAd(this).apply {
            setAdUnitId(adUnitId)
            setRewardedAdEventListener(eventLogger)
        }
    }

    private fun destroyRewarded() {
        rewardedAd?.destroy()
        rewardedAd = null
    }

    override fun onDestroy() {
        _adInfoFragment = null
        destroyRewarded()
        super.onDestroy()
    }

    private inner class RewardedAdEventLogger : RewardedAdEventListener {

        override fun onAdLoaded() {
            adInfoFragment.log("Rewarded ad loaded")
            adInfoFragment.hideLoading()
            rewardedAd?.show()
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.log(
                "Rewarded ad failed to load with code ${error.code}: ${error.description}"
            )
            adInfoFragment.hideLoading()
        }

        override fun onAdShown() {
            adInfoFragment.log("Rewarded ad shown")
        }

        override fun onAdDismissed() {
            adInfoFragment.log("Rewarded ad dismissed")
        }

        override fun onRewarded(reward: Reward) {
            adInfoFragment.log("Reward: ${reward.amount} of ${reward.type}")
        }

        override fun onAdClicked() {
            adInfoFragment.log("Rewarded ad clicked")
        }

        override fun onLeftApplication() {
            adInfoFragment.log("Left application")
        }

        override fun onReturnedToApplication() {
            adInfoFragment.log("Returned to application")
        }

        override fun onImpression(data: ImpressionData?) {
            adInfoFragment.log("Impression: ${data?.rawData}")
        }
    }

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-rewarded-yandex"),
            Network(R.drawable.ic_adcolony_icon_24, R.string.adcolony_title, "demo-rewarded-adcolony"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-rewarded-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-rewarded-applovin"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-rewarded-chartboost"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-rewarded-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-rewarded-mytarget"),
            Network(R.drawable.ic_ironsource_icon_24, R.string.ironsource_title, "demo-rewarded-ironsource"),
            Network(R.drawable.ic_pangle_icon, R.string.pangle_title, "demo-rewarded-pangle"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-rewarded-startapp"),
            Network(R.drawable.ic_tapjoy_icon, R.string.tapjoy_title, "demo-rewarded-tapjoy"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-rewarded-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-rewarded-vungle"),
        )
    }
}
