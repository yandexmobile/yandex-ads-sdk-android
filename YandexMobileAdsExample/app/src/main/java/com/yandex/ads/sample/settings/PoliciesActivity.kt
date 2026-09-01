/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityPoliciesBinding
import com.yandex.ads.sample.policy.PolicyAdapter
import com.yandex.ads.sample.policy.PolicyItem
import com.yandex.ads.sample.utils.ConsentManagementSampleUtils
import com.yandex.ads.sample.utils.applySystemBarsPadding
import com.yandex.mobile.ads.common.YandexAds

class PoliciesActivity : AppCompatActivity(R.layout.activity_policies) {

    private lateinit var binding: ActivityPoliciesBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPoliciesBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.toolbar.inflateMenu(R.menu.policies_menu)
        binding.toolbar.setOnMenuItemClickListener(::onMenuItemClick)
        setContentView(binding.root)
        applySystemBarsPadding(findViewById(R.id.coordinatorLayout))

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        binding.policies.layoutManager = LinearLayoutManager(this)
        binding.policies.adapter = PolicyAdapter(this, preferences, policies)
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_present_cmp -> {
                presentCmpForm()
                true
            }
            R.id.action_reset_cmp -> {
                ConsentManagementSampleUtils.resetConsentStatus(this)
                true
            }
            else -> false
        }
    }

    private fun presentCmpForm() {
        ConsentManagementSampleUtils.applyDebugParameters(this)
        ConsentManagementSampleUtils.presentConsentFormIfRequired(
            this,
            ConsentManagementSampleUtils.Trigger.MENU,
        )
    }

    companion object {

        const val VALUE = "value"

        private val policies = listOf(
            PolicyItem(
                R.drawable.ic_outline_handshake_24,
                R.string.gdpr_disabled,
                R.string.gdpr_enabled,
                GdprDialogFragment.TAG,
                ::GdprDialogFragment,
                YandexAds::setUserConsent,
            ),
            PolicyItem(
                R.drawable.ic_outline_place_24,
                R.string.location_disabled,
                R.string.location_enabled,
                LocationDialogFragment.TAG,
                ::LocationDialogFragment,
                YandexAds::setLocationTracking,
            ),
            PolicyItem(
                R.drawable.ic_outline_child_care_24,
                R.string.coppa_disabled,
                R.string.coppa_enabled,
                CoppaDialogFragment.TAG,
                ::CoppaDialogFragment,
                YandexAds::setAgeRestricted,
            ),
        )
    }
}
