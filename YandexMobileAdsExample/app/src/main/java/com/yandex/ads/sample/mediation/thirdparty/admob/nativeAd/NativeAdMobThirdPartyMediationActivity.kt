/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.admob.nativeAd

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.admob.mobileads.YandexNative
import com.admob.mobileads.nativeads.YandexNativeAdAsset.AGE
import com.admob.mobileads.nativeads.YandexNativeAdAsset.DOMAIN
import com.admob.mobileads.nativeads.YandexNativeAdAsset.FAVICON
import com.admob.mobileads.nativeads.YandexNativeAdAsset.FEEDBACK
import com.admob.mobileads.nativeads.YandexNativeAdAsset.RATING
import com.admob.mobileads.nativeads.YandexNativeAdAsset.REVIEW_COUNT
import com.admob.mobileads.nativeads.YandexNativeAdAsset.SPONSORED
import com.admob.mobileads.nativeads.YandexNativeAdAsset.WARNING
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityAdmobNativeThirdpartyMediationBinding
import com.yandex.ads.sample.utils.Logger

class NativeAdMobThirdPartyMediationActivity : AppCompatActivity() {

    private val adMobNativeAdBinder: AdMobNativeAdBinder = AdMobNativeAdBinder()

    private lateinit var binding: ActivityAdmobNativeThirdpartyMediationBinding

    private var nativeAdLoader: AdLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobNativeThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Replace demo Ad unit ID with actual Ad unit ID
        nativeAdLoader = AdLoader.Builder(this, AD_UNIT_ID)
            .withAdListener(NativeAdListener())
            .forNativeAd(NativeAdLoadedListener())
            .build()

        binding.nativeAdViewContainer.nativeAdView.isVisible = false
        binding.loadAdButton.setOnClickListener(::loadNativeAd)
    }

    private fun getCustomEventExtras(): Bundle {
        return bundleOf(
            AGE to R.id.age,
            DOMAIN to R.id.domain,
            FAVICON to R.id.favicon,
            FEEDBACK to R.id.feedback,
            RATING to R.id.domain,
            REVIEW_COUNT to R.id.review_count,
            SPONSORED to R.id.sponsored,
            WARNING to R.id.warning
        )
    }

    private fun loadNativeAd(view: View) {
        showLoading()

        val adRequest = AdRequest.Builder()
            .addCustomEventExtrasBundle(YandexNative::class.java, getCustomEventExtras())
            .build()
        nativeAdLoader?.loadAd(adRequest)
    }

    private fun showLoading() {
        binding.apply {
            nativeAdViewContainer.root.isVisible = false
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
        super.onDestroy()
        nativeAdLoader = null
    }

    private inner class NativeAdLoadedListener : OnNativeAdLoadedListener {

        override fun onNativeAdLoaded(nativeAd: NativeAd) {
            Logger.debug("onNativeAdLoaded")
            bindNativeAd(nativeAd)
            hideLoading()
        }

        private fun bindNativeAd(nativeAd: NativeAd) {
            val nativeAdAdmobViewBinding = binding.nativeAdViewContainer
            adMobNativeAdBinder.apply {
                clearNativeAdView(nativeAdAdmobViewBinding)
                bindNativeAd(nativeAdAdmobViewBinding, nativeAd)
            }
            binding.nativeAdViewContainer.nativeAdView.isVisible = true
        }
    }

    private inner class NativeAdListener : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            Logger.error(loadAdError.message)
        }
    }

    private companion object {

        const val AD_UNIT_ID = "ca-app-pub-4449457472880521/9429856591"
    }
}