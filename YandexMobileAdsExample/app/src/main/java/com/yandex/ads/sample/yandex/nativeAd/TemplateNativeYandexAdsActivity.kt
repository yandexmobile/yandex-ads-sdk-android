/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.nativeAd

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityTemplateNativeYandexAdsBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration

class TemplateNativeYandexAdsActivity : AppCompatActivity() {

    private val nativeAdLoadListener = NativeAdYandexAdsLoadListener()

    private var nativeAdLoader: NativeAdLoader? = null
    private lateinit var binding: ActivityTemplateNativeYandexAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemplateNativeYandexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadNativeAd)
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader?.setNativeAdLoadListener(nativeAdLoadListener)
    }

    private fun loadNativeAd(view: View) {
        showLoading()

        // Replace demo Ad Unit ID with actual Ad Unit ID
        val nativeAdRequestConfiguration = NativeAdRequestConfiguration.Builder(AD_UNIT_ID)
            .setShouldLoadImagesAutomatically(true)
            .build()

        nativeAdLoader?.loadAd(nativeAdRequestConfiguration)
    }

    private fun showLoading() {
        binding.apply {
            adLoadingProgress.isVisible = true
            nativeAdView.isVisible = false
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
        nativeAdLoader = null
        super.onDestroy()
    }

    private inner class NativeAdYandexAdsLoadListener : NativeAdLoadListener {

        override fun onAdLoaded(nativeAd: NativeAd) {
            Logger.debug("onAdLoaded")
            hideLoading()
            bindNativeAd(nativeAd)
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
            hideLoading()
        }
    }

    private fun bindNativeAd(nativeAd: NativeAd) {
        binding.nativeAdView.apply {
            setAd(nativeAd)
            isVisible = true
        }
    }

    private companion object {

        const val AD_UNIT_ID = "R-M-DEMO-native-i"
    }
}