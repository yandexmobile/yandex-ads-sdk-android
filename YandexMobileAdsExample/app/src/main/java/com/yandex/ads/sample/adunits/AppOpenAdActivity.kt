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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityAppOpenAdBinding
import com.yandex.ads.sample.utils.applySystemBarsPadding

class AppOpenAdActivity : AppCompatActivity(R.layout.activity_app_open_ad) {

    private lateinit var binding: ActivityAppOpenAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppOpenAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)
        applySystemBarsPadding(findViewById(R.id.coordinatorLayout))
    }
}
