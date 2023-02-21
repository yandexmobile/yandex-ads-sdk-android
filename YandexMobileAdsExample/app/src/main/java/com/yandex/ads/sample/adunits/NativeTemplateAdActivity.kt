/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityNativeTemplateAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration

class NativeTemplateAdActivity : AppCompatActivity(R.layout.activity_native_template_ad) {

    private val adInfoFragment get() = _adInfoFragment!!
    private val eventLogger = NativeAdEventLogger()

    private var nativeAdLoader: NativeAdLoader? = null
    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivityNativeTemplateAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeTemplateAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadNative
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
    }

    private fun loadNative() {
        createNative()
        nativeAdLoader?.loadAd(
            NativeAdRequestConfiguration
                .Builder(adInfoFragment.selectedNetwork.adUnitId)
                .setParameters(adFoxParameters)
                .setShouldLoadImagesAutomatically(true)
                .build()
        )
    }

    private fun bindNative(nativeAd: NativeAd) {
        nativeAd.setNativeAdEventListener(eventLogger)
        binding.nativeBanner.setAd(nativeAd)
    }

    private fun createNative() {
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader?.setNativeAdLoadListener(eventLogger)
    }

    override fun onDestroy() {
        nativeAdLoader = null
        _adInfoFragment = null
        super.onDestroy()
    }

    private inner class NativeAdEventLogger : NativeAdLoadListener, NativeAdEventListener {

        override fun onAdLoaded(ad: NativeAd) {
            adInfoFragment.log("Native ad loaded")
            adInfoFragment.hideLoading()
            bindNative(ad)
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.log(
                "Native ad failed to load with code ${error.code}: ${error.description}"
            )
            adInfoFragment.hideLoading()
        }

        override fun onAdClicked() {
            adInfoFragment.log("Native ad clicked")
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
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-native-content-yandex"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-native-admob"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-native-mytarget"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-native-startapp"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-10"),
        )

        private val adFoxParameters = mapOf(
            "adf_ownerid" to "270901",
            "adf_p1" to "cqtgi",
            "adf_p2" to "fksh"
        )
    }
}
