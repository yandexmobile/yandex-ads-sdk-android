/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityFlexBannerAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.utils.ScreenUtil.screenHeight
import com.yandex.ads.sample.utils.ScreenUtil.screenWidth
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class FlexBannerAdActivity : AppCompatActivity(R.layout.activity_flex_banner_ad) {

    private val adInfoFragment get() = _adInfoFragment!!
    private val eventLogger = BannerAdEventLogger()

    private var bannerWidth = 0
    private var bannerHeight = 0
    private var _adInfoFragment: AdInfoFragment? = null
    private var bannerAd: BannerAdView? = null

    private lateinit var binding: ActivityFlexBannerAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlexBannerAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadBanner
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        initBanner()
    }

    private fun initBanner() {
        bannerAd = binding.banner
        bannerWidth = screenWidth
        bannerHeight = screenHeight / 3
    }

    private fun loadBanner() {
        destroyBanner()
        createBanner()
        val adRequest = if (adInfoFragment.selectedNetwork.titleId == R.string.adfox_title) {
            adFoxRequest
        } else {
            AdRequest.Builder().build()
        }
        bannerAd?.loadAd(adRequest)
    }

    private fun createBanner() {
        bannerAd = BannerAdView(this).apply {
            id = R.id.banner
            setAdUnitId(adInfoFragment.selectedNetwork.adUnitId)
            setAdSize(AdSize.flexibleSize(bannerWidth, bannerHeight))
            setBannerAdEventListener(eventLogger)
        }
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
        ).apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }
        binding.root.addView(bannerAd, params)
    }

    private fun destroyBanner() {
        bannerAd?.let {
            it.destroy()
            binding.root.removeView(it)
        }
        bannerAd = null
    }

    override fun onDestroy() {
        _adInfoFragment = null
        destroyBanner()
        super.onDestroy()
    }

    private inner class BannerAdEventLogger : BannerAdEventListener {

        override fun onAdLoaded() {
            adInfoFragment.hideLoading()
            adInfoFragment.log("Banner ad loaded")
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.hideLoading()
            adInfoFragment.log(
                "Banner ad failed to load with code ${error.code}: ${error.description}"
            )
        }

        override fun onAdClicked() {
            adInfoFragment.log("Banner ad clicked")
        }

        override fun onLeftApplication() {
            adInfoFragment.log("Left application")
        }

        override fun onReturnedToApplication() {
            adInfoFragment.log("Returned to application")
        }

        override fun onImpression(data: ImpressionData?) {
            adInfoFragment.log("Impression: ${data?.rawData}")
        }
    }

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-banner-yandex"),
            Network(R.drawable.ic_adcolony_icon_24, R.string.adcolony_title, "demo-banner-adcolony"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-banner-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-banner-applovin"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-banner-chartboost"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-banner-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-banner-mytarget"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-banner-startapp"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-banner-vungle"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-8"),
        )

        private val adFoxRequest = AdRequest.Builder().setParameters(
            mapOf("adf_ownerid" to "270901", "adf_p1" to "cqtgh", "adf_p2" to "fkbd")
        ).build()
    }
}
