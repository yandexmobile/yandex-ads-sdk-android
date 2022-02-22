/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.admob.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.yandex.ads.sample.databinding.ActivityAdmobBannerThirdpartyMediationBinding
import com.yandex.ads.sample.utils.Logger

class BannerAdMobThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdmobBannerThirdpartyMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobBannerThirdpartyMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadAdButton.setOnClickListener(::loadBannerAd)

        configureAdMobView()
    }

    private fun configureAdMobView() {
        // NOTE: Replace demo Ad unit ID with actual Ad unit ID in attribute 'app:adUnitId'
        // for com.google.android.gms.ads.AdView in activity_admob_banner_thirdparty_mediation.xml
        binding.bannerAdmobView.adListener = BannerAdListener()
    }

    private fun loadBannerAd(view: View) {
        showLoading()

        binding.bannerAdmobView.apply {
            isVisible = false
            loadAd(AdRequest.Builder().build())
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

    private inner class BannerAdListener : AdListener() {

        override fun onAdLoaded() {
            binding.bannerAdmobView.isVisible = true
            Logger.debug("onInstreamAdPrepared")
            hideLoading()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            Logger.error(loadAdError.message)
            hideLoading()
        }
    }

    private companion object {

        const val AD_UNIT_ID = "ca-app-pub-4449457472880521/6430056371"
    }
}