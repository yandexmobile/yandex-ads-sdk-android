/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.NavigationListLayoutBinding
import com.yandex.ads.sample.yandex.nativeAd.NativeYandexAdsActivity
import com.yandex.ads.sample.navigation.NavigationListConfigurator
import com.yandex.ads.sample.navigation.NavigationListAdapter
import com.yandex.ads.sample.navigation.NavigationItem
import com.yandex.ads.sample.yandex.banner.BannerYandexAdsActivity
import com.yandex.ads.sample.yandex.instream.InstreamYandexAdsActivity
import com.yandex.ads.sample.yandex.interstitial.InterstitialYandexAdsActivity
import com.yandex.ads.sample.yandex.rewarded.RewardedYandexAdsActivity

class YandexAdsActivity : AppCompatActivity() {

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
            NavigationItem(BannerYandexAdsActivity::class.java, getString(R.string.banner_title)),
            NavigationItem(InterstitialYandexAdsActivity::class.java, getString(R.string.interstitial_title)),
            NavigationItem(RewardedYandexAdsActivity::class.java, getString(R.string.rewarded_title)),
            NavigationItem(NativeYandexAdsActivity::class.java, getString(R.string.native_title)),
            NavigationItem(InstreamYandexAdsActivity::class.java, getString(R.string.instream_title))
        )
    }
}

