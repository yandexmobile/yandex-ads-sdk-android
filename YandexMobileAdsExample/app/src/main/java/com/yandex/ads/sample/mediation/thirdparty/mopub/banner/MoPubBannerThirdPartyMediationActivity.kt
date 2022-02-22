/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.mopub.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.mopub.mobileads.DefaultBannerAdListener
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView
import com.yandex.ads.sample.databinding.ActivityMopubBannerThirdpartyMediationBinding
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializer
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubInitializerListener
import com.yandex.ads.sample.utils.Logger

class MoPubBannerThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMopubBannerThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMopubBannerThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadBannerAd)
        configureMoPubView()
    }

    private fun configureMoPubView() {
        binding.mopubView.apply {

            // Replace demo Ad unit ID with actual Ad unit ID
            setAdUnitId(AD_UNIT_ID)
            bannerAdListener = MoPubBannerAdEventListener()
            autorefreshEnabled = false
        }
    }

    private fun loadBannerAd(view: View) {
        showLoading()

        val moPubInitializerListener = object : MoPubInitializerListener {
            override fun onMoPubSdkInitialized() {
                binding.mopubView.loadAd()
            }
        }
        MoPubInitializer.initializeSdk(this, AD_UNIT_ID, moPubInitializerListener)
    }

    private fun showLoading() {
        binding.apply {
            mopubView.isVisible = false
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

    private inner class MoPubBannerAdEventListener : DefaultBannerAdListener() {

        override fun onBannerLoaded(banner: MoPubView) {
            Logger.debug("onBannerLoaded")
            binding.mopubView.isVisible = true
            hideLoading()
        }

        override fun onBannerFailed(banner: MoPubView, errorCode: MoPubErrorCode) {
            Logger.error("onBannerFailed with error $errorCode")
            hideLoading()
        }
    }

    private companion object {

        const val AD_UNIT_ID = "419d701f922942ce9099443346bdc0c3"
    }
}