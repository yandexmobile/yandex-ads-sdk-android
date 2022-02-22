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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.ActivityCustomNativeYandexAdsBinding
import com.yandex.ads.sample.databinding.NativeAdViewBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.*


class CustomNativeYandexAdsActivity : AppCompatActivity() {

    private val nativeAdLoadListener = NativeAdYandexAdsLoadListener()

    private val nativeAdView: NativeAdView
        get() = binding.nativeAdView.root

    private val adBinding: NativeAdViewBinding
        get() = binding.nativeAdView

    private var nativeAdLoader: NativeAdLoader? = null
    private lateinit var binding: ActivityCustomNativeYandexAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomNativeYandexAdsBinding.inflate(layoutInflater)
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
        nativeAdView.isVisible = false
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
        nativeAdLoader = null
        super.onDestroy()
    }

    private inner class NativeAdYandexAdsLoadListener : NativeAdLoadListener {

        override fun onAdLoaded(nativeAd: NativeAd) {
            Logger.debug( "onAdLoaded")
            bindNativeAd(nativeAd)
            hideLoading()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            Logger.error(adRequestError.description)
            hideLoading()
        }

        private fun bindNativeAd(nativeAd: NativeAd) {
            val nativeAdViewBinder = NativeAdViewBinder.Builder(nativeAdView)
                .setAgeView(adBinding.age)
                .setBodyView(adBinding.body)
                .setCallToActionView(adBinding.callToAction)
                .setDomainView(adBinding.domain)
                .setFaviconView(adBinding.favicon)
                .setFeedbackView(adBinding.feedback)
                .setIconView(adBinding.icon)
                .setMediaView(adBinding.media)
                .setPriceView(adBinding.price)
                .setRatingView(adBinding.rating)
                .setReviewCountView(adBinding.reviewCount)
                .setSponsoredView(adBinding.sponsored)
                .setTitleView(adBinding.title)
                .setWarningView(adBinding.warning)
                .build()

            configureMediaView(nativeAd)

            try {
                nativeAd.bindNativeAd(nativeAdViewBinder)
                nativeAdView.isVisible = true
            } catch (exception: NativeAdException) {
                Logger.error(exception.message.orEmpty())
            }
        }

        private fun configureMediaView(nativeAd: NativeAd) {
            val nativeAdMedia = nativeAd.adAssets.media
            // you can use the aspect ratio if you need it to determine the size of media view.
            val aspectRatio = nativeAdMedia?.aspectRatio
        }
    }

    private companion object {

        const val AD_UNIT_ID = "R-M-DEMO-native-i"
    }
}

