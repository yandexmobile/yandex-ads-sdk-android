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
import com.yandex.ads.sample.yandex.instream.advanced.InstreamBinderYandexAdsActivity
import com.yandex.ads.sample.yandex.instream.advanced.InstreamInrollYandexAdsActivity
import com.yandex.ads.sample.yandex.instream.simple.SimpleInstreamYandexAdsActivity

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
            NavigationItem(
                SimpleInstreamYandexAdsActivity::class.java,
                getString(R.string.instream_ad_simple_instream_title)
            ),
            NavigationItem(
                InstreamBinderYandexAdsActivity::class.java,
                getString(R.string.instream_ad_advanced_instream_binder_title)
            ),
            NavigationItem(
                InstreamInrollYandexAdsActivity::class.java,
                getString(R.string.instream_ad_advanced_instream_inroll_title)
            ),
        )
    }
}
