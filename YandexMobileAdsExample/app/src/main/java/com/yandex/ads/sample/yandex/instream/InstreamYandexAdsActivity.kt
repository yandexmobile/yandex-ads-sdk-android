/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.NavigationListLayoutBinding
import com.yandex.ads.sample.navigation.NavigationItem
import com.yandex.ads.sample.navigation.NavigationListAdapter
import com.yandex.ads.sample.navigation.NavigationListConfigurator
import com.yandex.ads.sample.yandex.instream.inroll.InrollYandexAdsActivity
import com.yandex.ads.sample.yandex.instream.simple.SimpleInstreamYandexAdsActivity
import com.yandex.ads.sample.yandex.instream.advanced.AdvancedInstreamYandexAdsActivity

class InstreamYandexAdsActivity : AppCompatActivity() {

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
            NavigationItem(AdvancedInstreamYandexAdsActivity::class.java, getString(R.string.instream_ad_advanced_instream_title)),
            NavigationItem(SimpleInstreamYandexAdsActivity::class.java, getString(R.string.instream_ad_simple_instream_title)),
            NavigationItem(InrollYandexAdsActivity::class.java, getString(R.string.instream_ad_inroll_title)),
        )
    }
}