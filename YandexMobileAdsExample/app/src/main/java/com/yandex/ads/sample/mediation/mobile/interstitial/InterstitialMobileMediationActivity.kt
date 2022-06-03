/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.mobile.interstitial

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityInterstitialMobileMediationBinding
import com.yandex.ads.sample.mediation.mobile.common.MediationNetwork
import com.yandex.ads.sample.mediation.mobile.common.MediationNetworkAdapter
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

class InterstitialMobileMediationActivity : AppCompatActivity() {

    private val interstitialAdEventListener = InterstitialMobileMediationAdEventListener()

    private var interstitialAd: InterstitialAd? = null

    private lateinit var binding: ActivityInterstitialMobileMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialMobileMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadAdButton.setOnClickListener(::loadInterstitialAd)

        // Replace demo Ad Unit ID with actual Ad Unit ID
        binding.mediationProvidersSpinner.adapter = MediationNetworkAdapter(mediationNetworks, this)
    }

    private fun configureInterstitialAd(){
        val adUnitId = getAdUnitId()
        interstitialAd?.apply {
            setAdUnitId(adUnitId)
            setInterstitialAdEventListener(interstitialAdEventListener)
        }
    }

    private fun loadInterstitialAd(view: View) {
        destroyInterstitialAd()
        interstitialAd = InterstitialAd(this)
        configureInterstitialAd()
        showLoading()

        interstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun getAdUnitId(): String {
        val position = binding.mediationProvidersSpinner.selectedItemPosition

        // Replace demo Ad Unit ID with actual Ad Unit ID
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
        destroyInterstitialAd()
        super.onDestroy()
    }

    private fun destroyInterstitialAd() {
        interstitialAd?.setInterstitialAdEventListener(null)
        interstitialAd?.destroy()
        interstitialAd = null
    }

    private inner class InterstitialMobileMediationAdEventListener : InterstitialAdEventListener {

        override fun onAdLoaded() {
            Logger.debug("onAdLoaded")
            interstitialAd?.show()
            hideLoading()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
            hideLoading()
        }

        override fun onAdClicked() {
            Logger.debug( "onAdClicked")
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

        override fun onLeftApplication() {
            Logger.debug("onLeftApplication")
        }

        override fun onReturnedToApplication() {
            Logger.debug("onReturnedToApplication")
        }
    }

    private companion object {

        val mediationNetworks = listOf(
            MediationNetwork(R.string.yandex_title, "R-M-338228-8"),
            MediationNetwork(R.string.adcolony_title, "R-M-344723-17"),
            MediationNetwork(R.string.admob_title, "R-M-338228-14"),
            MediationNetwork(R.string.applovin_title, "R-M-338228-33"),
            MediationNetwork(R.string.facebook_title, "R-M-338228-17"),
            MediationNetwork(R.string.chartboost_title, "R-M-344723-16"),
            MediationNetwork(R.string.my_target_title, "R-M-338228-16"),
            MediationNetwork(R.string.ironsource_title, "R-M-338228-36"),
            MediationNetwork(R.string.pangle_title, "R-M-344723-22"),
            MediationNetwork(R.string.startapp_title, "R-M-338228-29"),
            MediationNetwork(R.string.tapjoy_title, "R-M-344723-18"),
            MediationNetwork(R.string.unity_ads_title, "R-M-338228-26"),
            MediationNetwork(R.string.vungle_title, "R-M-344723-20"),
        )
    }
}
