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
import com.yandex.ads.sample.databinding.ActivityInterstitialAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.network.NetworkAdapter
import com.yandex.ads.sample.utils.applySystemBarsPadding
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader

class InterstitialAdActivity : AppCompatActivity(R.layout.activity_interstitial_ad),
    InterstitialAdLoadListener {

    private val eventLogger = InterstitialAdEventLogger()
    private var interstitialAdLoader: InterstitialAdLoader? = null

    private var selectedIndex: Int = 0
    private val selectedNetwork get() = networks[selectedIndex]
    private var interstitialAd: InterstitialAd? = null

    private lateinit var binding: ActivityInterstitialAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInterstitialAdBinding.inflate(layoutInflater)
        binding.setupUiBidding()
        setContentView(binding.root)
        applySystemBarsPadding(findViewById(R.id.coordinatorLayout))

        // Interstitial ads loading should occur after initialization of the SDK.
        // Initialize SDK as early as possible, for example in Application.onCreate or at least Activity.onCreate
        // It's recommended to use the same instance of InterstitialAdLoader for every load for
        // achieve better performance
        interstitialAdLoader = InterstitialAdLoader(this).apply {
            setAdLoadListener(this@InterstitialAdActivity)
        }
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        disableShowAdButton()
        interstitialAdLoader?.loadAd(createAdRequestConfiguration())
    }

    private fun createAdRequestConfiguration(): AdRequestConfiguration {
        return if (selectedNetwork.titleId == R.string.adfox_title) {
            AdRequestConfiguration.Builder(selectedNetwork.adUnitId)
                .setParameters(adFoxRequestParameters)
        } else {
            AdRequestConfiguration.Builder(selectedNetwork.adUnitId)
        }.build()
    }

    private fun showInterstitialAd() {
        disableShowAdButton()
        interstitialAd?.apply {
            setAdEventListener(eventLogger)
            show(this@InterstitialAdActivity)
        }
    }

    override fun onAdLoaded(interstitialAd: InterstitialAd) {
        log("Interstitial ad loaded")
        this.interstitialAd = interstitialAd
        enableShowAdButton()
    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        log(
            "Interstitial ad ${adRequestError.adUnitId} failed to load " +
                "with code ${adRequestError.code}: ${adRequestError.description}"
        )
        disableShowAdButton()
    }

    private fun destroyInterstitial() {
        // don't forget to clean up event listener to null?
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    override fun onDestroy() {
        // set listener to null to avoid memory leaks
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        destroyInterstitial()
        super.onDestroy()
    }

    private inner class InterstitialAdEventLogger : InterstitialAdEventListener {

        override fun onAdShown() {
            log("Interstitial ad shown")
        }

        override fun onAdFailedToShow(adError: AdError) {
            log("Interstitial ad show error: $adError")
        }

        override fun onAdDismissed() {
            log("Interstitial ad dismissed")
            destroyInterstitial()
            // Now you can preload the next interstitial ad.
            loadInterstitialAd()
        }

        override fun onAdClicked() {
            log("Interstitial ad clicked")
        }

        override fun onAdImpression(data: ImpressionData?) {
            log("Impression: ${data?.rawData}")
        }
    }

    private fun ActivityInterstitialAdBinding.setupUiBidding() {
        showAdButton.setOnClickListener { showInterstitialAd() }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        networkMenu.setStartIconDrawable(selectedNetwork.iconId)
        networkTextView.setText(selectedNetwork.titleId)
        networkTextView.setAdapter(NetworkAdapter(this@InterstitialAdActivity, networks))
        networkTextView.setOnItemClickListener { _, _, position, _ ->
            selectedIndex = position
            networkMenu.setStartIconDrawable(selectedNetwork.iconId)
            loadInterstitialAd()
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
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-interstitial-yandex"),
            Network(R.drawable.ic_admanager_icon, R.string.admanager_title, "demo-interstitial-admanager"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-interstitial-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-interstitial-applovin"),
            Network(R.drawable.ic_appnext_icon, R.string.appnext_title, "demo-interstitial-appnext"),
            Network(R.drawable.ic_bigoads_icon, R.string.bigoads_title, "demo-interstitial-bigoads"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-interstitial-chartboost"),
            Network(R.drawable.ic_inmobi_icon, R.string.inmobi_title, "demo-interstitial-inmobi"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-interstitial-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-interstitial-mytarget"),
            Network(R.drawable.ic_ironsource_icon_24, R.string.ironsource_title, "demo-interstitial-ironsource"),
            Network(R.drawable.ic_pangle_icon, R.string.pangle_title, "demo-interstitial-pangle"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-interstitial-startapp"),
            Network(R.drawable.ic_tapjoy_icon, R.string.tapjoy_title, "demo-interstitial-tapjoy"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-interstitial-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-interstitial-vungle"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-8"),
        )

        private val adFoxRequestParameters =
            mapOf("adf_ownerid" to "270901", "adf_p1" to "cqtgg", "adf_p2" to "fhlx")
    }
}
