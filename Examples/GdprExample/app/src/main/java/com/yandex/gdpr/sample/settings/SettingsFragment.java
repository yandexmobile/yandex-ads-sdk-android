/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.gdpr.sample.settings;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.TwoStatePreference;

import com.yandex.gdpr.sample.R;
import com.yandex.mobile.ads.common.MobileAds;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String DIALOG_SHOWN_KEY = "show_dialog";
    public static final String USER_CONSENT_KEY = "preference_user_consent";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
        if (USER_CONSENT_KEY.equals(preference.getKey())) {
            final boolean userConsent = ((TwoStatePreference) preference).isChecked();
            MobileAds.setUserConsent(userConsent);
        }

        return super.onPreferenceTreeClick(preference);
    }
}
