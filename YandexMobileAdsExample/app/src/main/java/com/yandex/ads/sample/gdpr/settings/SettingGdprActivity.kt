/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.gdpr.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R

class SettingGdprActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_gdpr)
        supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingGdprFragment()).commit()
    }
}
