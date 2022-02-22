/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfox.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityBannerAdfoxBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class BannerAdFoxActivity : AppCompatActivity() {

    private val bannerAdEventListener = BannerEventListener()

    private lateinit var binding: ActivityBannerAdfoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerAdfoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadBannerAd)
        configureBannerView()
    }

    private fun configureBannerView() {
        binding.bannerAdView.apply {
            setAdUnitId(AD_UNIT_ID)
            setAdSize(AdSize.BANNER_300x300)
            setBannerAdEventListener(bannerAdEventListener)
        }
    }

    private fun loadBannerAd(view: View) {

        binding.apply {
            showLoading()

            // Replace demo adfox parameters with actual adfox parameters
            val adRequest = AdRequest.Builder().setParameters(ADFOX_PARAMETERS).build()
            bannerAdView.loadAd(adRequest)
        }
    }

    private fun showLoading() {
        binding.apply {
            adLoadingProgress.isVisible = true
            bannerAdView.isVisible = false
            loadAdButton.isEnabled = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            adLoadingProgress.isVisible = false
            loadAdButton.isEnabled = true
        }
    }

    private inner class BannerEventListener : BannerAdEventListener {

        override fun onAdLoaded() {
            Logger.debug("onAdLoaded")
            binding.bannerAdView.isVisible = true
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
            "adf_p1" to "cqtgh",
            "adf_p2" to "fkbd",
        )
        const val AD_UNIT_ID = "R-M-243655-8"
    }
}