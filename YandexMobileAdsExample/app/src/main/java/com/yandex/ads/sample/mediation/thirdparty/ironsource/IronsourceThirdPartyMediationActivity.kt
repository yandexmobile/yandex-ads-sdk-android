/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.ironsource

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.NavigationListLayoutBinding
import com.yandex.ads.sample.mediation.thirdparty.ironsource.interstital.InterstitialIronsourceThirdPartyMediationActivity
import com.yandex.ads.sample.mediation.thirdparty.ironsource.rewarded.RewardedIronsourceThirdPartyMediationActivity
import com.yandex.ads.sample.navigation.NavigationItem
import com.yandex.ads.sample.navigation.NavigationListAdapter
import com.yandex.ads.sample.navigation.NavigationListConfigurator

class IronsourceThirdPartyMediationActivity : AppCompatActivity() {

    private lateinit var binding: NavigationListLayoutBinding

    private val navigationListConfigurator = NavigationListConfigurator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavigationListLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationListConfigurator.configure(this, binding.navigationList)
        binding.navigationList.adapter = NavigationListAdapter(getNavigationItems())
    }

    private fun getNavigationItems(): List<NavigationItem> {
        return listOf(
            NavigationItem(InterstitialIronsourceThirdPartyMediationActivity::class.java, getString(R.string.interstitial_title)),
            NavigationItem(RewardedIronsourceThirdPartyMediationActivity::class.java, getString(R.string.rewarded_title)),
        )
    }
}