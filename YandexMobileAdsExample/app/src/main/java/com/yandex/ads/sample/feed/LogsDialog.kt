/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2024 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.feed

import android.app.AlertDialog
import android.content.Context
import com.yandex.ads.sample.R

class LogsDialog(context: Context) {

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setPositiveButton(context.getString(R.string.ok_button)) { dialog, _ ->
            dialog.dismiss()
        }
        .create()

    fun show(logs: String) {
        alertDialog.apply {
            setTitle(context.getString(R.string.logs))
            setMessage(logs)

            show()
        }
    }

    fun setLogs(logs: String) {
        alertDialog.setMessage(logs)
    }
}

