/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.utils

import android.util.Log
import android.widget.Toast
import com.yandex.ads.sample.Application

object Logger {

    private const val TAG = "AdsSample"

    private lateinit var application: Application

    fun initialize(application: Application) {
        this.application = application
    }

    fun debug(message: String) {
        Log.d(TAG, message)
    }

    fun error(message: String) {
        Log.e(TAG, message)
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }
}