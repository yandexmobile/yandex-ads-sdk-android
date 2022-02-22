/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfox

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.adfox.banner.BannerAdFoxActivity
import com.yandex.ads.sample.adfox.interstitial.InterstitialAdFoxActivity
import com.yandex.ads.sample.adfox.nativeAd.NativeAdFoxActivity
import com.yandex.ads.sample.databinding.NavigationListLayoutBinding
import com.yandex.ads.sample.navigation.NavigationListConfigurator
import com.yandex.ads.sample.navigation.NavigationListAdapter
import com.yandex.ads.sample.navigation.NavigationItem

class AdFoxActivity : AppCompatActivity() {

    private val navigationListConfigurator = NavigationListConfigurator()

    private lateinit var binding: NavigationListLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavigationListLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationListConfigurator.configure(this, binding.navigationList)
        binding.navigationList.adapter = NavigationListAdapter(getNavigationItems())
    }

    private fun getNavigationItems(): List<NavigationItem> {
        return listOf(
            NavigationItem(BannerAdFoxActivity::class.java, getString(R.string.banner_title)),
            NavigationItem(InterstitialAdFoxActivity::class.java, getString(R.string.interstitial_title)),
            NavigationItem(NativeAdFoxActivity::class.java, getString(R.string.native_title))
        )
    }
}