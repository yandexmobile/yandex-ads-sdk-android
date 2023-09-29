/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityCustomNativeAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdException
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder
import org.json.JSONException
import org.json.JSONObject

class CustomNativeAdActivity : AppCompatActivity(R.layout.activity_custom_native_ad) {

    private val nativeAdView get() = binding.nativeAd.root
    private val additionalContainer get() = binding.additionalContainer
    private val adInfoFragment get() = requireNotNull(_adInfoFragment)
    private val eventLogger = NativeAdEventLogger()

    private var nativeAdLoader: NativeAdLoader? = null
    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivityCustomNativeAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomNativeAdBinding.inflate(layoutInflater)
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
        additionalContainer.removeAllViews()
        nativeAdView.isVisible = false
        nativeAdLoader?.loadAd(buildAdRequestConfiguration())
    }

    private fun createNative() {
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader?.setNativeAdLoadListener(eventLogger)
    }

    private fun buildAdRequestConfiguration(): NativeAdRequestConfiguration {
        val adUnitId = adInfoFragment.selectedNetwork.adUnitId
        return NativeAdRequestConfiguration
            .Builder(adUnitId)
            .setShouldLoadImagesAutomatically(true)
            .apply {
                if (adUnitId == AD_FOX_AD_UNIT_ID) {
                    setParameters(adFoxParameters)
                }
            }.build()
    }

    private fun bindNative(nativeAd: NativeAd) {
        val nativeAdViewBinder = binding.nativeAd.run {
            NativeAdViewBinder.Builder(nativeAdView)
                .setAgeView(age)
                .setBodyView(body)
                .setCallToActionView(callToAction)
                .setDomainView(domain)
                .setFaviconView(favicon)
                .setFeedbackView(feedback)
                .setIconView(icon)
                .setMediaView(media)
                .setPriceView(price)
                .setRatingView(rating)
                .setReviewCountView(reviewCount)
                .setSponsoredView(sponsored)
                .setTitleView(title)
                .setWarningView(warning)
                .build()
        }

        try {
            nativeAd.bindNativeAd(nativeAdViewBinder)
            nativeAd.info?.let { processAdditionalText(it) }
            nativeAd.setNativeAdEventListener(eventLogger)
            nativeAdView.isVisible = true
        } catch (exception: NativeAdException) {
            Logger.error(exception.message.orEmpty())
        }
    }

    private fun processAdditionalText(rawJson: String) {
        try {
            val jsonObject = JSONObject(rawJson)
            val contentJSONArray = jsonObject.getJSONArray("content")

            repeat(contentJSONArray.length()) { index ->
                val elementName = contentJSONArray.getString(index)
                val elementConfiguration = jsonObject.getJSONObject(elementName)

                when (elementName) {
                    "text" -> addTextToLayout(elementConfiguration, index)
                    "button" -> addButtonToLayout(elementConfiguration, index)
                }
            }
        } catch (e: JSONException) {
            adInfoFragment.log(e.message ?: "JSON parsing error")
        }
    }

    private fun addTextToLayout(textConfiguration: JSONObject, index: Int) {
        val textView = TextView(this).apply {
            setPadding(resources.getDimension(R.dimen.item_padding).toInt())

            text = textConfiguration.getString("value")
        }
        additionalContainer.addView(textView, index)
    }

    private fun addButtonToLayout(buttonConfiguration: JSONObject, index: Int) {
        val buttonView = Button(this).apply {
            setPadding(resources.getDimension(R.dimen.item_padding).toInt())

            text = buttonConfiguration.getString("label")

            val colorHex = buttonConfiguration.getString("backgroundColor")
            setBackgroundColor(Color.parseColor(colorHex))

            setOnClickListener {
                val link = buttonConfiguration.getString("link")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }
        additionalContainer.addView(buttonView, index)
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

        private const val AD_FOX_AD_UNIT_ID = "R-M-243655-10"

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-native-content-yandex"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, AD_FOX_AD_UNIT_ID),
        )

        private val adFoxParameters = mapOf(
            "adf_ownerid" to "270901",
            "adf_p1" to "czlie",
            "adf_p2" to "fksh"
        )
    }
}
