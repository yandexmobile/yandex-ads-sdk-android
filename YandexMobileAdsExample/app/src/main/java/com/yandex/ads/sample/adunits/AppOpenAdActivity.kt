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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityAppOpenAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.network.NetworkAdapter
import com.yandex.ads.sample.utils.applySystemBarsPadding
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader

class AppOpenAdActivity : AppCompatActivity(R.layout.activity_app_open_ad),
    AppOpenAdLoadListener {

    private lateinit var binding: ActivityAppOpenAdBinding

    private var appOpenAdLoader: AppOpenAdLoader? = null
    private var appOpenAd: AppOpenAd? = null

    private var selectedIndex: Int = 0
    private val selectedNetwork get() = networks[selectedIndex]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppOpenAdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarsPadding(findViewById(R.id.coordinatorLayout))

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.setupUi()

        appOpenAdLoader = AppOpenAdLoader(this).apply {
            setAdLoadListener(this@AppOpenAdActivity)
        }

        loadAppOpenAd()
    }

    private fun ActivityAppOpenAdBinding.setupUi() {
        showAdButton.setOnClickListener { showAppOpenAd() }
        networkMenu.setStartIconDrawable(selectedNetwork.iconId)
        networkTextView.setText(selectedNetwork.titleId)
        networkTextView.setAdapter(NetworkAdapter(this@AppOpenAdActivity, networks))
        networkTextView.setOnItemClickListener { _, _, position, _ ->
            selectedIndex = position
            networkMenu.setStartIconDrawable(selectedNetwork.iconId)
            loadAppOpenAd()
        }
    }

    private fun loadAppOpenAd() {
        disableShowAdButton()
        appOpenAdLoader?.loadAd(createAdRequestConfiguration())
    }

    private fun createAdRequestConfiguration(): AdRequestConfiguration {
        return AdRequestConfiguration.Builder(selectedNetwork.adUnitId).build()
    }

    private fun showAppOpenAd() {
        disableShowAdButton()
        appOpenAd?.apply {
            setAdEventListener(AppOpenEventLogger())
            show(this@AppOpenAdActivity)
        }
    }

    override fun onAdLoaded(ad: AppOpenAd) {
        log("AppOpen ad loaded")
        appOpenAd = ad
        enableShowAdButton()
    }

    override fun onAdFailedToLoad(error: AdRequestError) {
        log("AppOpen ad ${error.adUnitId} failed to load with code ${error.code}: ${error.description}")
        disableShowAdButton()
        appOpenAd = null
    }

    private fun destroyAppOpen() {
        appOpenAd?.setAdEventListener(null)
        appOpenAd = null
    }

    override fun onDestroy() {
        appOpenAdLoader?.setAdLoadListener(null)
        appOpenAdLoader = null
        destroyAppOpen()
        super.onDestroy()
    }

    private inner class AppOpenEventLogger : AppOpenAdEventListener {

        override fun onAdShown() {
            log("AppOpen ad shown")
        }

        override fun onAdFailedToShow(adError: AdError) {
            log("AppOpen ad show error: $adError")
            loadAppOpenAd()
        }

        override fun onAdDismissed() {
            log("AppOpen ad dismissed")
            destroyAppOpen()
            loadAppOpenAd()
        }

        override fun onAdClicked() {
            log("AppOpen ad clicked")
        }

        override fun onAdImpression(data: ImpressionData?) {
            log("Impression: ${data?.rawData}")
        }
    }

    private fun enableShowAdButton() {
        binding.showAdButton.isEnabled = true
    }

    private fun disableShowAdButton() {
        binding.showAdButton.isEnabled = false
    }

    private fun log(message: String) {
        binding.log.text = getString(R.string.log_format, binding.log.text, message)
    }

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-appopenad-admob"),
        )
    }
}
