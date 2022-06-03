/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.mobile.nativeAd

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityNativeMobileMediationBinding
import com.yandex.ads.sample.mediation.mobile.common.MediationNetwork
import com.yandex.ads.sample.mediation.mobile.common.MediationNetworkAdapter
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration

class NativeMobileMediationActivity : AppCompatActivity() {

    private val nativeAdLoadListener = NativeAdMobileMediationLoadListener()
    private val nativeAdEventListener = NativeAdMobileMediationEventListener()

    private var nativeAdLoader: NativeAdLoader? = null
    private lateinit var binding: ActivityNativeMobileMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeMobileMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadAdButton.setOnClickListener(::loadNativeAd)

        // Replace demo Ad Unit ID with actual Ad Unit ID
        binding.mediationProvidersSpinner.adapter = MediationNetworkAdapter(mediationNetworks, this)
    }

    private fun createNativeAdLoader() {
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader?.setNativeAdLoadListener(nativeAdLoadListener)
    }

    private fun getAdUnitId(): String {
        val position = binding.mediationProvidersSpinner.selectedItemPosition

        // Replace demo Ad Unit ID with actual Ad Unit ID
        return mediationNetworks[position].adUnitId
    }

    private fun bindNativeAd(nativeAd: NativeAd) {
        nativeAd.setNativeAdEventListener(nativeAdEventListener)
        binding.nativeBanner.apply {
            setAd(nativeAd)
            isVisible = true
        }
    }

    private fun loadNativeAd(view: View) {
        createNativeAdLoader()

        showLoading()

        val adUnitId = getAdUnitId()
        val nativeAdRequestConfiguration = NativeAdRequestConfiguration.Builder(adUnitId)
            .setShouldLoadImagesAutomatically(true)
            .build()

        nativeAdLoader?.loadAd(nativeAdRequestConfiguration)
    }

    private fun showLoading() {
        binding.apply {
            nativeBanner.isVisible = false
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
        nativeAdLoader = null
        super.onDestroy()
    }

    private inner class NativeAdMobileMediationLoadListener : NativeAdLoadListener {

        override fun onAdLoaded(nativeAd: NativeAd) {
            Logger.debug("onAdLoaded")
            bindNativeAd(nativeAd)
            hideLoading()
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            Logger.error(error.description)
            hideLoading()
        }
    }

    private inner class NativeAdMobileMediationEventListener : NativeAdEventListener {

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

        val mediationNetworks = listOf(
            MediationNetwork(R.string.yandex_title, "R-M-338228-11"),
            MediationNetwork(R.string.admob_title, "R-M-338228-22"),
            MediationNetwork(R.string.facebook_title, "R-M-338228-25"),
            MediationNetwork(R.string.my_target_title, "R-M-338228-24"),
            MediationNetwork(R.string.startapp_title, "R-M-338228-39"),
        )
    }
}
