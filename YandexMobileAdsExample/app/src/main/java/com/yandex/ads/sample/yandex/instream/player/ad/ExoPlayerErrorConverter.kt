/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.ads.sample.yandex.instream.player.ad

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.yandex.mobile.ads.instream.player.ad.error.InstreamAdPlayerError

class ExoPlayerErrorConverter {

    fun convertExoPlayerError(throwable: Throwable): InstreamAdPlayerError {
        return when (throwable) {
            is ExoPlaybackException -> {
                val cause = throwable.cause
                return if (cause != null) {
                    convertExoPlayerError(cause);
                } else {
                    InstreamAdPlayerError(InstreamAdPlayerError.Reason.UNKNOWN, throwable)
                }
            }
            is HttpDataSource.CleartextNotPermittedException -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.INVALID_FILE, throwable)
            }
            is HttpDataSource.InvalidResponseCodeException -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.FILE_NOT_FOUND, throwable)
            }
            is HttpDataSource.HttpDataSourceException -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.NETWORK_UNAVAILABLE, throwable)
            }
            is UnrecognizedInputFormatException -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.UNSUPPORTED_FILE_FORMAT, throwable)
            }
            is MediaCodecRenderer.DecoderInitializationException -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.UNSUPPORTED_CODEC, throwable)
            }
            is MediaCodecUtil.DecoderQueryException -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.UNSUPPORTED_CODEC, throwable)
            }
            else -> {
                InstreamAdPlayerError(InstreamAdPlayerError.Reason.UNKNOWN, throwable)
            }
        }
    }
}