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

class CoppaDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.coppa_dialog_title)
            .setMessage(R.string.coppa_dialog_message)
            .setPositiveButton(R.string.coppa_underage) { _, _ -> setResult(true) }
            .setNegativeButton(R.string.coppa__not_underage) { _, _ -> setResult(false) }
            .create()
    }

    private fun setResult(result: Boolean) {
        // Example result, consider specifying user's age instead
        setFragmentResult(TAG, bundleOf(PoliciesActivity.VALUE to result))
    }

    companion object {

        const val TAG = "coppa"
    }
}
