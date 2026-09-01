/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.yandex.ads.sample.R
import com.yandex.mobile.ads.consentmanagement.ConsentManagementDebugParameters
import com.yandex.mobile.ads.consentmanagement.ConsentManagementPlatform
import com.yandex.mobile.ads.consentmanagement.ConsentManagementPresentationListener
import com.yandex.mobile.ads.consentmanagement.ConsentManagementPresentationResult
import java.security.MessageDigest

object ConsentManagementSampleUtils {

    private const val LOG_PREFIX = "CMP"
    private const val RESET_STATUS_LOG_DELAY_MS = 500L

    enum class Trigger {
        MENU,
        GDPR_DIALOG,
    }

    private val consentManagementPlatform: ConsentManagementPlatform
        get() = ConsentManagementPlatform

    private val mainHandler = Handler(Looper.getMainLooper())

    fun applyDebugParameters(context: Context) {
        val testDeviceId = resolveUmpTestDeviceHashedId(context)
        consentManagementPlatform.setDebugParameters(
            ConsentManagementDebugParameters(
                testDeviceIdentifiers = listOfNotNull(testDeviceId),
                geography = ConsentManagementDebugParameters.Geography.EEA,
            )
        )
        logDebug(
            context.getString(
                R.string.cmp_debug_params_applied,
                testDeviceId ?: context.getString(R.string.cmp_test_device_unavailable),
            ),
        )
    }

    fun presentConsentFormIfRequired(context: Context, trigger: Trigger) {
        logDebug(context.getString(R.string.cmp_debug_present_requested, trigger.name))
        consentManagementPlatform.presentConsentFormIfRequired(
            context,
            object : ConsentManagementPresentationListener {
                override fun onConsentManagementPresentationCompleted(result: ConsentManagementPresentationResult) {
                    when (result) {
                        ConsentManagementPresentationResult.SUCCESS ->
                            logInfo(context.getString(R.string.cmp_message_success))
                        ConsentManagementPresentationResult.NOT_REQUIRED ->
                            logInfo(context.getString(R.string.cmp_message_not_required))
                    }
                }

                override fun onConsentManagementPresentationFailed(error: Throwable) {
                    logError(
                        context.getString(
                            R.string.cmp_message_presentation_failed,
                            error.message ?: error.javaClass.simpleName,
                        ),
                    )
                }
            },
        )
    }

    fun resetConsentStatus(context: Context) {
        logDebug(context.getString(R.string.cmp_debug_reset_requested))
        consentManagementPlatform.resetConsentStatus(context)
        mainHandler.postDelayed({
            logInfo(context.getString(R.string.cmp_message_reset_done))
        }, RESET_STATUS_LOG_DELAY_MS)
    }

    private fun resolveUmpTestDeviceHashedId(context: Context): String? {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: return null
        return MessageDigest.getInstance("MD5")
            .digest(androidId.toByteArray())
            .joinToString(separator = "") { byte -> "%02X".format(byte) }
    }

    private fun logDebug(message: String) {
        Logger.debug("$LOG_PREFIX: $message")
    }

    private fun logInfo(message: String) {
        Logger.info("$LOG_PREFIX: $message")
    }

    private fun logError(message: String) {
        Logger.error("$LOG_PREFIX: $message")
    }
}
