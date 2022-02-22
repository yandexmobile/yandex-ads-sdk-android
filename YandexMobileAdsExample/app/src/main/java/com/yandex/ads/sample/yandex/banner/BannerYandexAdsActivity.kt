/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityBannerYandexAdsBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class BannerYandexAdsActivity : AppCompatActivity() {

    private val bannerAdEventListener = BannerAdYandexAdsEventListener()

    private lateinit var binding: ActivityBannerYandexAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerYandexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadBannerAd)
        configureBannerView()
    }

    private fun configureBannerView() {
        binding.bannerAdView.apply {

            // Replace demo Ad Unit ID with actual Ad Unit ID
            setAdUnitId(AD_UNIT_ID)
            setAdSize(AdSize.BANNER_320x50)
            setBannerAdEventListener(bannerAdEventListener)
        }
    }

    private fun loadBannerAd(view: View) {
        showLoading()
        binding.bannerAdView.loadAd(AdRequest.Builder().build())
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

    private inner class BannerAdYandexAdsEventListener : BannerAdEventListener {

        override fun onAdLoaded() {
            Logger.debug("onAdLoaded")
            binding.bannerAdView.isVisible = true
            hideLoading()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
            hideLoading()
        }

        override fun onImpression(impressionData: ImpressionData?) {
            Logger.debug("onImpression")
        }

        override fun onAdClicked() {
            Logger.debug( "onAdClicked")
        }

        override fun onLeftApplication() {
            Logger.debug( "onLeftApplication")
        }

        override fun onReturnedToApplication() {
            Logger.debug( "onReturnedToApplication")
        }
    }

    private companion object {

        const val AD_UNIT_ID = "R-M-DEMO-320x50"
    }
}