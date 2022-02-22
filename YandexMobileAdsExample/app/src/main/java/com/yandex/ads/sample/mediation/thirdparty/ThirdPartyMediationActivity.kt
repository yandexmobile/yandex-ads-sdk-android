/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.NavigationListLayoutBinding
import com.yandex.ads.sample.mediation.thirdparty.admob.AdMobThirdPartyMediationActivity
import com.yandex.ads.sample.mediation.thirdparty.ironsource.IronsourceThirdPartyMediationActivity
import com.yandex.ads.sample.mediation.thirdparty.mopub.MoPubThirdPartyMediationActivity
import com.yandex.ads.sample.navigation.NavigationListConfigurator
import com.yandex.ads.sample.navigation.NavigationListAdapter
import com.yandex.ads.sample.navigation.NavigationItem

class ThirdPartyMediationActivity : AppCompatActivity() {

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
            NavigationItem(AdMobThirdPartyMediationActivity::class.java, getString(R.string.admob_title)),
            NavigationItem(MoPubThirdPartyMediationActivity::class.java, getString(R.string.mopub_title)),
            NavigationItem(IronsourceThirdPartyMediationActivity::class.java, getString(R.string.ironsource_title))
        )
    }
}