/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.mopub.nativeAd

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.mopub.nativeads.AdapterHelper
import com.mopub.nativeads.MoPubStaticNativeAdRenderer
import com.mopub.nativeads.ViewBinder
import com.mopub.nativeads.YandexViewBinder
import com.mopub.nativeads.YandexNativeAdRenderer
import com.mopub.nativeads.RequestParameters
import com.mopub.nativeads.MoPubNative
import com.mopub.nativeads.NativeErrorCode
import com.mopub.nativeads.NativeAd
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityMopubNativeThirdpartyMediationBinding
import com.yandex.ads.sample.databinding.NativeAdMopubImageBinding
import com.yandex.ads.sample.databinding.NativeAdYandexBinding
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializer
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializerListener
import com.yandex.ads.sample.utils.Logger

class MoPubNativeThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMopubNativeThirdpartyMediationBinding

    private var moPubNative: MoPubNative? = null
    private var adapterHelper: AdapterHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMopubNativeThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadMoPubNativeAd)

        moPubNative = MoPubNative(this, AD_UNIT_ID, MoPubNativeListener())
        configureNativeAd()
    }

    private fun configureNativeAd() {
        adapterHelper = AdapterHelper(
            this,
            MOPUB_ADAPTER_SHOW_AD_START_POSITION,
            MOPUB_ADAPTER_SHOW_AD_INTERVAL
        )

        // Replace demo Ad unit ID with actual Ad unit ID
        moPubNative = MoPubNative(this, AD_UNIT_ID, MoPubNativeListener())
        registerNativeAdRenderers()
    }

    private fun registerNativeAdRenderers() {

        val nativeAdMoPubImageBinding = NativeAdMopubImageBinding.inflate(layoutInflater)
        val nativeAdYandexBinding = NativeAdYandexBinding.inflate(layoutInflater)

        val moPubViewBinder: ViewBinder
        nativeAdMoPubImageBinding.apply {
            moPubViewBinder = ViewBinder.Builder(R.layout.native_ad_mopub_image)
                .callToActionId(callToAction.id)
                .iconImageId(icon.id)
                .mainImageId(image.id)
                .textId(body.id)
                .titleId(title.id)
                .privacyInformationIconImageId(privacyInformationIcon.id)
                .build()
        }
        val yandexViewBinder: YandexViewBinder

        nativeAdYandexBinding.apply {
            yandexViewBinder =
                YandexViewBinder.Builder(R.layout.native_ad_yandex)
                .setAgeId(age.id)
                .setBodyId(body.id)
                .setCallToActionId(callToAction.id)
                .setDomainId(domain.id)
                .setFaviconId(favicon.id)
                .setIconId(icon.id)
                .setMediaId(media.id)
                .setPriceId(price.id)
                .setRatingId(rating.id)
                .setReviewCountId(reviewCount.id)
                .setSponsoredId(sponsored.id)
                .setTitleId(title.id)
                .setWarningId(warning.id)
                .build()
        }
        moPubNative?.registerAdRenderer(MoPubStaticNativeAdRenderer(moPubViewBinder))
        moPubNative?.registerAdRenderer(YandexNativeAdRenderer(yandexViewBinder))
    }


    private fun loadMoPubNativeAd(view: View) {
        showLoading()

        val listener = object : MoPubInitializerListener {
            override fun onMoPubSdkInitialized() {
                moPubNative?.makeRequest(RequestParameters.Builder().build())
            }
        }

        // Replace demo Ad unit ID with actual Ad unit ID
        MoPubInitializer.initializeSdk(this, AD_UNIT_ID, listener)
    }

    private fun showLoading() {
        binding.apply {
            nativeAdContainer.isVisible = false
            loadAdButton.isEnabled = false
            adLoadingProgress.isVisible = true
        }
    }

    private fun hideLoading() {
        binding.apply {
            adLoadingProgress.isVisible = false
            loadAdButton.isEnabled = true
        }
    }

    override fun onDestroy() {
        moPubNative?.destroy()
        moPubNative = null
        super.onDestroy()
    }

    private fun renderNativeAdView(nativeAd: NativeAd) {
        val nativeAdView = adapterHelper?.getAdView(null, binding.nativeAdContainer, nativeAd)

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        binding.nativeAdContainer.apply {
            removeAllViews()
            addView(nativeAdView, layoutParams)
            isVisible = true
        }
    }

    private inner class MoPubNativeListener : MoPubNativeNetworkListener {

        override fun onNativeLoad(nativeAd: NativeAd) {
            Logger.debug("onInterstitialDismissed")
            renderNativeAdView(nativeAd)
            hideLoading()
        }

        override fun onNativeFail(errorCode: NativeErrorCode) {
            Logger.error("Ad failed to load: $errorCode")
            hideLoading()
        }
    }

    private companion object {

        const val MOPUB_ADAPTER_SHOW_AD_START_POSITION = 0
        const val MOPUB_ADAPTER_SHOW_AD_INTERVAL = 2
        const val AD_UNIT_ID = "25d8379e4af94cae861f91ed3c9b2af4"
    }
}