/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample

import android.app.Dialog
import android.os.Bundle
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NetworkWarningDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.warning)
            .setMessage(R.string.network_warning)
            .setPositiveButton(R.string.ok) { _, _ -> setDialogShown() }
            .create()
    }

    private fun setDialogShown() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        preferences.edit { putBoolean(HomeActivity.NETWORK_WARNING_SHOWN, true) }
    }
}
