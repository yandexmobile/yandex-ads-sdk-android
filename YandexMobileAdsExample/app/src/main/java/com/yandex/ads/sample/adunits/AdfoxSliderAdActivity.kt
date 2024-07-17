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
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adfoxslider.AdfoxSliderAdapter
import com.yandex.ads.sample.adfoxslider.SliderAutoscroller
import com.yandex.ads.sample.adfoxslider.SliderIndicatorController
import com.yandex.ads.sample.databinding.ActivityAdfoxSliderAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder
import com.yandex.mobile.ads.nativeads.SliderAd
import com.yandex.mobile.ads.nativeads.SliderAdLoadListener
import com.yandex.mobile.ads.nativeads.SliderAdLoader

class AdfoxSliderAdActivity : AppCompatActivity(R.layout.activity_adfox_slider_ad) {

    private val sliderAdView get() = binding.sliderNativeAdView
    private val sliderAdViewPager2 get() = binding.sliderAdViewPager2
    private val sliderAdapter get() = sliderAdViewPager2.adapter as AdfoxSliderAdapter
    private val leftButton get() = binding.sliderAdLeftButton
    private val rightButton get() = binding.sliderAdRightButton
    private val indicatorLayout get() = binding.sliderAdIndicatorLayout
    private val adInfoFragment get() = requireNotNull(_adInfoFragment)
    private val eventLogger = AdfoxSliderEventLogger()

    private var sliderAutoscroller: SliderAutoscroller? = null
    private var sliderIndicatorController: SliderIndicatorController? = null
    private var sliderAdLoader: SliderAdLoader? = null
    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivityAdfoxSliderAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdfoxSliderAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadAds
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        sliderAutoscroller = SliderAutoscroller(sliderAdViewPager2)
        sliderIndicatorController = SliderIndicatorController(indicatorLayout)

        configureAdLoader()
        configureViewPager()
    }

    override fun onDestroy() {
        sliderAutoscroller?.stopScrolling()
        sliderAutoscroller = null
        sliderIndicatorController = null
        sliderAdLoader = null
        _adInfoFragment = null
        super.onDestroy()
    }

    private fun loadAds() {
        sliderAdLoader?.loadSlider(buildAdRequestConfiguration())
    }

    private fun configureAdLoader() {
        sliderAdLoader = SliderAdLoader(this)
        sliderAdLoader?.setSliderAdLoadListener(eventLogger)
    }

    private fun configureViewPager() {
        sliderAdViewPager2.adapter = AdfoxSliderAdapter()
        sliderAdViewPager2.registerOnPageChangeCallback(ViewPagerCallback())
        configureViewPagerPadding()
        configureViewPagerControls()
    }

    private fun buildAdRequestConfiguration(): NativeAdRequestConfiguration {
        val adUnitId = adInfoFragment.selectedNetwork.adUnitId
        return NativeAdRequestConfiguration
            .Builder(adUnitId)
            .setShouldLoadImagesAutomatically(true)
            .setParameters(adFoxParameters)
            .build()
    }

    private fun bindSliderAd(sliderAd: SliderAd) {
        val sliderViewBinder = NativeAdViewBinder.Builder(sliderAdView).build()
        sliderAd.bindSliderAd(sliderViewBinder)
        bindNativeAds(sliderAd.nativeAds)
    }

    private fun bindNativeAds(nativeAds: List<NativeAd>) {
        nativeAds.forEach { it.setNativeAdEventListener(eventLogger) }
        sliderAdapter.setData(nativeAds)
        sliderIndicatorController?.setupIndicator(nativeAds.size, sliderAdViewPager2.currentItem)
        sliderAutoscroller?.startScrolling()
        setViewPagerControlsVisibility()
    }

    private fun configureViewPagerPadding() {
        val padding = resources.getDimension(R.dimen.adfox_slider_ad_padding).toInt()
        val recyclerView = sliderAdViewPager2.getChildAt(0) as RecyclerView
        recyclerView.apply {
            setPadding(padding, 0, padding, 0)
            clipToPadding = false
        }
    }

    private fun configureViewPagerControls() {
        leftButton.setOnClickListener {
            val previousItem = (sliderAdViewPager2.currentItem + sliderAdapter.itemCount - 1) % sliderAdapter.itemCount
            sliderAdViewPager2.setCurrentItem(previousItem, true)
        }
        rightButton.setOnClickListener {
            val nextItem = (sliderAdViewPager2.currentItem + 1) % sliderAdapter.itemCount
            sliderAdViewPager2.setCurrentItem(nextItem, true)
        }
    }

    private fun setViewPagerControlsVisibility() {
        val shouldShow = sliderAdapter.itemCount > 1
        leftButton.isVisible = shouldShow
        rightButton.isVisible = shouldShow
        indicatorLayout.isVisible = shouldShow
    }

    private inner class AdfoxSliderEventLogger : SliderAdLoadListener, NativeAdEventListener {

        override fun onSliderAdLoaded(sliderAd: SliderAd) {
            adInfoFragment.log("Ads loaded")
            adInfoFragment.hideLoading()
            bindSliderAd(sliderAd)
        }

        override fun onSliderAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.log(
                "Ads failed to load with code ${error.code}: ${error.description}"
            )
            adInfoFragment.hideLoading()
        }

        override fun onAdClicked() {
            adInfoFragment.log("Ad clicked")
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

    private inner class ViewPagerCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setViewPagerControlsVisibility()
            sliderIndicatorController?.changeSelectedPage(position)
            sliderAutoscroller?.updateLastScroll()
        }
    }

    companion object {

        private const val AD_FOX_AD_UNIT_ID = "R-M-243655-8"

        private val networks = arrayListOf(
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, AD_FOX_AD_UNIT_ID),
        )

        private val adFoxParameters = mapOf(
            "adf_ownerid" to "270901",
            "adf_p1" to "ddflb",
            "adf_p2" to "fksh"
        )
    }
}
