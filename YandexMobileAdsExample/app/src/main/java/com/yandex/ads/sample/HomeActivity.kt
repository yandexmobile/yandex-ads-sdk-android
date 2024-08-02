/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yandex.ads.sample.adunits.AdfoxCarouselAdActivity
import com.yandex.ads.sample.adunits.AppOpenAdActivity
import com.yandex.ads.sample.adunits.CustomNativeAdActivity
import com.yandex.ads.sample.adunits.FeedActivity
import com.yandex.ads.sample.adunits.InlineBannerAdActivity
import com.yandex.ads.sample.adunits.InstreamAdBinderActivity
import com.yandex.ads.sample.adunits.InstreamAdInrollActivity
import com.yandex.ads.sample.adunits.InterstitialAdActivity
import com.yandex.ads.sample.adunits.NativeTemplateAdActivity
import com.yandex.ads.sample.adunits.RewardedAdActivity
import com.yandex.ads.sample.adunits.SimpleInstreamAdActivity
import com.yandex.ads.sample.adunits.StickyBannerAdActivity
import com.yandex.ads.sample.databinding.ActivityHomeBinding
import com.yandex.ads.sample.navigation.NavigationAdapter
import com.yandex.ads.sample.navigation.NavigationItem
import com.yandex.ads.sample.navigation.NavigationItem.NavigationType.ActivityNavigation
import com.yandex.ads.sample.navigation.NavigationItem.NavigationType.DebugPanelNavigation
import com.yandex.ads.sample.settings.PoliciesActivity

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNetworkWarningDialog()

        binding.adTypes.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(this)
            } else {
                GridLayoutManager(this, 2)
            }
        binding.adTypes.adapter = NavigationAdapter(adTypeItems)
    }

    private fun showNetworkWarningDialog() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isNetworkWarningDialogShown = preferences.getBoolean(NETWORK_WARNING_SHOWN, false)
        if (isNetworkWarningDialogShown.not()) {
            NetworkWarningDialogFragment().show(supportFragmentManager, NETWORK_WARNING)
        }
    }

    companion object {

        const val NETWORK_WARNING_SHOWN = "networkWarningShown"
        private const val NETWORK_WARNING = "networkWarning"

        private val adTypeItems = listOf(
            NavigationItem(
                R.drawable.ic_outline_ad_units_24,
                R.string.sticky_banner_title,
                ActivityNavigation(StickyBannerAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_aspect_ratio_24,
                R.string.inline_banner_title,
                ActivityNavigation(InlineBannerAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_fullscreen_24,
                R.string.interstitial_title,
                ActivityNavigation(InterstitialAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_video_library_24,
                R.string.rewarded_title,
                ActivityNavigation(RewardedAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_developer_mode_24,
                R.string.native_template_title,
                ActivityNavigation(NativeTemplateAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_developer_board_24,
                R.string.native_custom_title,
                ActivityNavigation(CustomNativeAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_carousel_24,
                R.string.adfox_carousel_title,
                ActivityNavigation(AdfoxCarouselAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_movie_24,
                R.string.instream_simple_title,
                ActivityNavigation(SimpleInstreamAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_movie_filter_24,
                R.string.instream_binder_title,
                ActivityNavigation(InstreamAdBinderActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_video_settings_24,
                R.string.instream_inroll_title,
                ActivityNavigation(InstreamAdInrollActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_child_care_24,
                R.string.policies,
                ActivityNavigation(PoliciesActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_full_coverage_24,
                R.string.appopenad_title,
                ActivityNavigation(AppOpenAdActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_feed_24,
                R.string.feed_title,
                ActivityNavigation(FeedActivity::class.java),
            ),
            NavigationItem(
                R.drawable.ic_outline_instruments_24,
                R.string.debug_panel,
                DebugPanelNavigation
            )
        )
    }
}
