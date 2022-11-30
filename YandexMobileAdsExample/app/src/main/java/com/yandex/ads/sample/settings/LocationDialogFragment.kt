/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.settings

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yandex.ads.sample.R

class LocationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.location_dialog_title)
            .setMessage(R.string.location_dialog_message)
            .setNegativeButton(R.string.decline) { _, _ -> setResult(false) }
            .setPositiveButton(R.string.accept) { _, _ -> setResult(true) }
            .create()
    }

    private fun setResult(result: Boolean) {
        setFragmentResult(TAG, bundleOf(PoliciesActivity.VALUE to result))
    }

    companion object {

        const val TAG = "location"
    }
}
