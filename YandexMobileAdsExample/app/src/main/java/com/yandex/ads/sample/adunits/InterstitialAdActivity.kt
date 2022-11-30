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
import com.yandex.ads.sample.databinding.ActivityInterstitialAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

class InterstitialAdActivity : AppCompatActivity(R.layout.activity_interstitial_ad) {

    private val adInfoFragment get() = _adInfoFragment!!
    private val eventLogger = InterstitialAdEventLogger()

    private var adUnitId = ""
    private var _adInfoFragment: AdInfoFragment? = null
    private var interstitialAd: InterstitialAd? = null

    private lateinit var binding: ActivityInterstitialAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadInterstitial
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
    }

    private fun loadInterstitial() {
        if (adUnitId != adInfoFragment.selectedNetwork.adUnitId) {
            adUnitId = adInfoFragment.selectedNetwork.adUnitId
            destroyInterstitial()
            createInterstitial()
        }
        val adRequest = if (adInfoFragment.selectedNetwork.titleId == R.string.adfox_title) {
            adFoxRequest
        } else {
            AdRequest.Builder().build()
        }
        interstitialAd?.loadAd(adRequest)
    }

    private fun createInterstitial() {
        interstitialAd = InterstitialAd(this).apply {
            setAdUnitId(adUnitId)
            setInterstitialAdEventListener(eventLogger)
        }
    }

    private fun destroyInterstitial() {
        interstitialAd?.destroy()
        interstitialAd = null
    }

    override fun onDestroy() {
        _adInfoFragment = null
        destroyInterstitial()
        super.onDestroy()
    }

    private inner class InterstitialAdEventLogger : InterstitialAdEventListener {

        override fun onAdLoaded() {
            adInfoFragment.log("Interstitial ad loaded")
            adInfoFragment.hideLoading()
            interstitialAd?.show()
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.log(
                "Interstitial ad failed to load with code ${error.code}: ${error.description}"
            )
            adInfoFragment.hideLoading()
        }

        override fun onAdShown() {
            adInfoFragment.log("Interstitial ad shown")
        }

        override fun onAdDismissed() {
            adInfoFragment.log("Interstitial ad dismissed")
        }

        override fun onAdClicked() {
            adInfoFragment.log("Interstitial ad clicked")
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
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-interstitial-yandex"),
            Network(R.drawable.ic_adcolony_icon_24, R.string.adcolony_title, "demo-interstitial-adcolony"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-interstitial-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-interstitial-applovin"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-interstitial-chartboost"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-interstitial-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-interstitial-mytarget"),
            Network(R.drawable.ic_ironsource_icon_24, R.string.ironsource_title, "demo-interstitial-ironsource"),
            Network(R.drawable.ic_pangle_icon, R.string.pangle_title, "demo-interstitial-pangle"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-interstitial-startapp"),
            Network(R.drawable.ic_tapjoy_icon, R.string.tapjoy_title, "demo-interstitial-tapjoy"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-interstitial-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-interstitial-vungle"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-8"),
        )

        private val adFoxRequest = AdRequest.Builder().setParameters(
            mapOf("adf_ownerid" to "270901", "adf_p1" to "cqtgg", "adf_p2" to "fhlx")
        ).build()
    }
}
