/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfox.interstitial

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityInterstitialAdfoxBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

class InterstitialAdFoxActivity : AppCompatActivity() {

    private val interstitialAdEventListener = InterstitialAdYandexAdsEventListener()

    private var interstitialAd: InterstitialAd? = null
    private lateinit var binding: ActivityInterstitialAdfoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialAdfoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadInterstitialAd)
        interstitialAd = InterstitialAd(this)
        configureInterstitialAd()
    }

    private fun loadInterstitialAd(view: View) {
        showLoading()

        // Replace demo adfox parameters with actual adfox parameters
        val adRequest = AdRequest.Builder().setParameters(ADFOX_PARAMETERS).build()
        interstitialAd?.loadAd(adRequest)
    }

    private fun configureInterstitialAd() {
        interstitialAd?.apply {
            setAdUnitId(AD_UNIT_ID)
            setInterstitialAdEventListener(interstitialAdEventListener)
        }
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
        interstitialAd?.destroy()
        interstitialAd = null
        super.onDestroy()
    }

    private inner class InterstitialAdYandexAdsEventListener : InterstitialAdEventListener {

        override fun onAdLoaded() {
            Logger.debug("onAdLoaded")
            hideLoading()
            interstitialAd?.show()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
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

        val ADFOX_PARAMETERS = mapOf(
            "adf_ownerid" to "270901",
            "adf_p1" to "cqtgg",
            "adf_p2" to "fhlx"
        )
        const val AD_UNIT_ID = "R-M-243655-9"
    }
}