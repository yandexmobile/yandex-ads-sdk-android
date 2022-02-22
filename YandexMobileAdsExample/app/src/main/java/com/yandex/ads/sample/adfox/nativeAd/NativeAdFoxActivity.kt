/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfox.nativeAd

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityNativeAdfoxBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration

class NativeAdFoxActivity : AppCompatActivity() {

    private val nativeAdLoadListener = NativeAdYandexAdsLoadListener()

    private var nativeAdLoader: NativeAdLoader? = null

    private lateinit var binding: ActivityNativeAdfoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeAdfoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadNativeAd)
        configureNativeAdLoader()
    }

    private fun configureNativeAdLoader() {
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader?.setNativeAdLoadListener(nativeAdLoadListener)
    }

    private fun loadNativeAd(view: View) {
        showLoading()

        // Replace demo adfox parameters with actual adfox parameters
        val nativeAdRequestConfiguration = NativeAdRequestConfiguration.Builder(AD_UNIT_ID)
            .setParameters(ADFOX_PARAMETERS)
            .setShouldLoadImagesAutomatically(true)
            .build()

        nativeAdLoader?.loadAd(nativeAdRequestConfiguration)
    }

    private fun showLoading() {
        binding.apply {
            adLoadingProgress.isVisible = true
            loadAdButton.isEnabled = false
            nativeAdView.isVisible = false
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

        val ADFOX_PARAMETERS = mapOf(
            "adf_ownerid" to "270901",
            "adf_p1" to "cqtgi",
            "adf_p2" to "fksh"
        )
        const val AD_UNIT_ID = "R-M-243655-10"
    }
}