/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.gdpr

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yandex.ads.sample.R
import com.yandex.ads.sample.gdpr.settings.SettingGdprFragment

class GdprDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // This is sample GDPR dialog which is used to demonstrate the GDPR user consent retrieval logic.
        // Please, do not use this dialog in production app.
        // Replace it with one which is suitable for the app.
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.gdpr_dialog_title)
            .setMessage(R.string.gdpr_dialog_message)
            .setPositiveButton(R.string.gdpr_dialog_accept) { _, _ ->
                setResult(true)
            }
            .setNeutralButton(R.string.gdpr_dialog_about) { _, _ ->
                openPrivacyPolicy()
            }
            .setNegativeButton(R.string.gdpr_dialog_decline) { _, _ ->
                setResult(false)
            }
            .create()
    }

    private fun openPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
        startActivity(intent)
    }

    private fun setResult(userConsent: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        preferences.edit {
            putBoolean(SettingGdprFragment.USER_CONSENT_KEY, userConsent)
            putBoolean(SettingGdprFragment.DIALOG_SHOWN_KEY, true)
        }

        setFragmentResult(REQUEST_KEY, bundleOf())
    }

    companion object {

        const val REQUEST_KEY = "GdprDialogFragment"
        private const val PRIVACY_POLICY_URL = "https://yandex.com/legal/confidential/"
    }
}