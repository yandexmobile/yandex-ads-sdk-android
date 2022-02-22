/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.mobile.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityBannerMobileMediationBinding
import com.yandex.ads.sample.mediation.mobile.common.MediationNetwork
import com.yandex.ads.sample.mediation.mobile.common.MediationNetworkAdapter
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class BannerMobileMediationActivity : AppCompatActivity() {

    private val bannerAdEventListener = BannerMobileMediationAdEventListener()

    private var bannerAdView: BannerAdView? = null
    private lateinit var binding: ActivityBannerMobileMediationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerMobileMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Replace demo Ad Unit ID with actual Ad Unit ID
        binding.mediationProvidersSpinner.adapter = MediationNetworkAdapter(mediationNetworks, this)
        binding.loadAdButton.setOnClickListener(::loadBannerAd)
    }

    private fun loadBannerAd(view: View) {
        destroyBannerAdView()
        bannerAdView = BannerAdView(this)

        attachBannerAdView()
        configureBannerAdView()
        showLoading()

        bannerAdView?.loadAd(AdRequest.Builder().build())
    }

    private fun destroyBannerAdView() {
        if (bannerAdView != null) {
            binding.root.removeView(bannerAdView)
        }
        bannerAdView?.destroy()
        bannerAdView = null
    }

    private fun attachBannerAdView() {

        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }

        binding.root.addView(bannerAdView, params)
    }

    private fun configureBannerAdView() {
        val adUnitId = getadUnitId()

        bannerAdView?.apply {
            setAdUnitId(adUnitId)
            setAdSize(AdSize.BANNER_320x50)
            setBannerAdEventListener(bannerAdEventListener)
        }
    }

    private fun getadUnitId(): String {
        val position = binding.mediationProvidersSpinner.selectedItemPosition

        // Replace demo Ad Unit ID with actual Ad Unit ID
        return mediationNetworks[position].adUnitId
    }

    private fun showLoading() {
        binding.apply {
            adLoadingProgress.isVisible = true
            loadAdButton.isEnabled = false
        }
        bannerAdView?.isVisible = false
    }

    private fun hideLoading() {
        binding.apply {
            adLoadingProgress.isVisible = false
            loadAdButton.isEnabled = true
        }
    }

    override fun onDestroy() {
        destroyBannerAdView()
        super.onDestroy()
    }

    private inner class BannerMobileMediationAdEventListener : BannerAdEventListener {

        override fun onAdLoaded() {
            Logger.debug("onAdLoaded")
            bannerAdView?.isVisible = true
            hideLoading()
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            Logger.error(error.description)
            hideLoading()
        }

        override fun onAdClicked() {
            Logger.debug( "onAdClicked")
        }

        override fun onImpression(impressionData: ImpressionData?) {
            Logger.debug("onImpression")
        }

        override fun onLeftApplication() {
            Logger.debug("onRewardedAdStarted")
        }

        override fun onReturnedToApplication() {
            Logger.debug("onReturnedToApplication")
        }
    }

    private companion object{

        val mediationNetworks = listOf(
            MediationNetwork(R.string.yandex_title, "adf-279013/966631"),
            MediationNetwork(R.string.adcolony_title, "adf-279013/1196978"),
            MediationNetwork(R.string.admob_title, "adf-279013/975926"),
            MediationNetwork(R.string.applovin_title, "adf-279013/1049726"),
            MediationNetwork(R.string.facebook_title, "adf-279013/975929"),
            MediationNetwork(R.string.chartboost_title, "adf-279013/1197235"),
            MediationNetwork(R.string.mopub_title, "adf-279013/975927"),
            MediationNetwork(R.string.my_target_title, "adf-279013/975928"),
            MediationNetwork(R.string.startapp_title, "adf-279013/1004807"),
            MediationNetwork(R.string.vungle_title, "adf-279013/1198306"),
        )
    }
}