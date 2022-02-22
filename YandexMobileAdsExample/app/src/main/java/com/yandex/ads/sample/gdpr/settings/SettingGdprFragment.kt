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
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference
import com.yandex.ads.sample.R
import com.yandex.mobile.ads.common.MobileAds

class SettingGdprFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference is TwoStatePreference && preference.key == USER_CONSENT_KEY) {
            MobileAds.setUserConsent(preference.isChecked)
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {

        const val DIALOG_SHOWN_KEY = "show_dialog"
        const val USER_CONSENT_KEY = "preference_user_consent"
    }
}