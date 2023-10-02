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
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityStickyBannerAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import kotlin.math.roundToInt

class StickyBannerAdActivity : AppCompatActivity(R.layout.activity_sticky_banner_ad) {

    private val adInfoFragment get() = requireNotNull(_adInfoFragment)
    private val eventLogger = BannerAdEventLogger()

    private var _adInfoFragment: AdInfoFragment? = null
    private var bannerAd: BannerAdView? = null
    private var currentAdUnitId: String? = null
    private var bannerAdSize: BannerAdSize? = null

    private lateinit var binding: ActivityStickyBannerAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStickyBannerAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadBanner
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        bannerAd = binding.banner
        configureAdSize()
    }

    private fun configureAdSize() {
        binding.coordinatorLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.coordinatorLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val adWidthPixels = binding.coordinatorLayout.width
                val adWidthDp = (adWidthPixels / resources.displayMetrics.density).roundToInt()

                // For achieve the best performance of sticky banner BannerAdSize.stickySize
                // should be called after SDK initialization.
                // Initialize SDK as early as possible, for example in Application.onCreate
                // or at least Activity.onCreate
                bannerAdSize = BannerAdSize.stickySize(this@StickyBannerAdActivity, adWidthDp)
            }
        })
    }

    private fun loadBanner() {
        bannerAdSize?.let { bannerAdSize ->
            val selectedAdUnitId = adInfoFragment.selectedNetwork.adUnitId
            if (currentAdUnitId != selectedAdUnitId) {
                destroyBanner()
                createBanner(selectedAdUnitId, bannerAdSize)
            }
            val adRequest = AdRequest.Builder()
                .setParameters(getRequestParameters())
                .build()
            bannerAd?.loadAd(adRequest)
            adInfoFragment.showLoading()
        }
    }

    private fun createBanner(adUnitId: String, bannerAdSize: BannerAdSize) {
        bannerAd = BannerAdView(this).apply {
            id = R.id.banner
            setAdUnitId(adUnitId)
            currentAdUnitId = adUnitId
            setAdSize(bannerAdSize)
            setBannerAdEventListener(eventLogger)
        }
        // you can use sticky banner size in your layout
        val params = ConstraintLayout.LayoutParams(
            bannerAdSize.getWidthInPixels(this),
            bannerAdSize.getHeightInPixels(this),
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
        currentAdUnitId = null
    }

    override fun onDestroy() {
        _adInfoFragment = null
        destroyBanner()
        super.onDestroy()
    }

    private fun getRequestParameters(): Map<String, String> {
        return if (adInfoFragment.selectedNetwork.titleId == R.string.adfox_title) {
            return mapOf("adf_ownerid" to "270901", "adf_p1" to "cqtgg", "adf_p2" to "fhlx")
        } else emptyMap()
    }

    private inner class BannerAdEventLogger : BannerAdEventListener {

        override fun onAdLoaded() {
            adInfoFragment.log("Banner ad loaded")
            adInfoFragment.hideLoading()
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.log(
                "Banner ad failed to load with code ${error.code}: ${error.description}"
            )
            adInfoFragment.hideLoading()
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
            Network(R.drawable.ic_inmobi_icon, R.string.inmobi_title, "demo-banner-inmobi"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-banner-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-banner-mytarget"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-banner-startapp"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-banner-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-banner-vungle"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-8"),
        )
    }
}
