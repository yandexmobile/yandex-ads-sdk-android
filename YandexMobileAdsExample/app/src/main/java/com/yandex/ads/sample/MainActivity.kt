/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.adfox.AdFoxActivity
import com.yandex.ads.sample.databinding.NavigationListLayoutBinding
import com.yandex.ads.sample.gdpr.GdprActivity
import com.yandex.ads.sample.mediation.mobile.MobileMediationActivity
import com.yandex.ads.sample.mediation.thirdparty.ThirdPartyMediationActivity
import com.yandex.ads.sample.navigation.NavigationItem
import com.yandex.ads.sample.navigation.NavigationListAdapter
import com.yandex.ads.sample.navigation.NavigationListConfigurator
import com.yandex.ads.sample.yandex.YandexAdsActivity

class MainActivity : AppCompatActivity() {

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
            NavigationItem(YandexAdsActivity::class.java, getString(R.string.yandex_ads_title)),
            NavigationItem(MobileMediationActivity::class.java, getString(R.string.mobile_mediation_title)),
            NavigationItem(ThirdPartyMediationActivity::class.java, getString(R.string.third_party_mediation_title)),
            NavigationItem(GdprActivity::class.java, getString(R.string.gdpr_navigation_title)),
            NavigationItem(AdFoxActivity::class.java, getString(R.string.adfox_navigation_title))
        )
    }
}