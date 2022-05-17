/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.ads.sample.yandex.instream.player.ad

import android.media.MediaCodec
import android.media.MediaDrmResetException
import android.media.ResourceBusyException
import android.os.Build
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoTimeoutException
import com.google.android.exoplayer2.IllegalSeekPositionException
import com.google.android.exoplayer2.ParserException
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.audio.DefaultAudioSink
import com.google.android.exoplayer2.drm.DrmSession
import com.google.android.exoplayer2.drm.KeysExpiredException
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.text.SubtitleDecoderException
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.Loader
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.video.MediaCodecVideoDecoderException
import com.yandex.mobile.ads.instream.player.ad.error.InstreamAdPlayerError
import java.net.HttpURLConnection
import javax.net.ssl.SSLHandshakeException

class ExoPlayerErrorConverter {

    fun convertExoPlayerError(throwable: Throwable): InstreamAdPlayerError {
        val reason = getReason(throwable)
        return InstreamAdPlayerError(reason, throwable)
    }

    private fun getReason(throwable: Throwable): InstreamAdPlayerError.Reason {
        return when (throwable) {
            is ExoPlaybackException -> {
                getReasonErrorInRendering(throwable)
                    ?: throwable.cause?.let { getReason(it) }
                    ?: InstreamAdPlayerError.Reason.UNKNOWN
            }
            is ExoTimeoutException -> {
                InstreamAdPlayerError.Reason.TIMEOUT
            }
            is IllegalSeekPositionException -> {
                InstreamAdPlayerError.Reason.ILLEGAL_SEEK_POSITION
            }
            is MediaCodecUtil.DecoderQueryException -> {
                InstreamAdPlayerError.Reason.DECODER_QUERY_ERROR
            }
            is MediaCodecRenderer.DecoderInitializationException -> {
                InstreamAdPlayerError.Reason.DECODER_INITIALIZATION_ERROR
            }
            is MediaCodecVideoDecoderException -> {
                getReasonErrorInRendering(throwable) ?: InstreamAdPlayerError.Reason.DECODER_UNKNOWN_ERROR
            }
            is BehindLiveWindowException -> {
                InstreamAdPlayerError.Reason.BEHIND_LIVE_WINDOW_ERROR
            }
            is MediaCodec.CryptoException -> {
                InstreamAdPlayerError.Reason.DRM_KEYS_EXPIRED
            }
            is DrmSession.DrmSessionException -> {
                getReasonErrorDrmSession(throwable)
            }
            is HttpDataSource.CleartextNotPermittedException -> {
                InstreamAdPlayerError.Reason.HTTP_CLEARTEXT_NOT_PERMITTED
            }
            is HttpDataSource.InvalidResponseCodeException -> {
                getReasonErrorHttp(throwable)
            }
            is HttpDataSource.HttpDataSourceException -> {
                getReasonErrorConnection(throwable)
            }
            is ParserException -> {
                InstreamAdPlayerError.Reason.CONTENT_PARSER_ERROR
            }
            is Loader.UnexpectedLoaderException -> {
                InstreamAdPlayerError.Reason.LOADER_UNEXPECTED_ERROR
            }
            is AudioSink.ConfigurationException,
            is AudioSink.InitializationException,
            is DefaultAudioSink.InvalidAudioTrackTimestampException -> {
                InstreamAdPlayerError.Reason.AUDIO_ERROR
            }
            is SubtitleDecoderException -> {
                InstreamAdPlayerError.Reason.SUBTITLE_ERROR
            }
            is Cache.CacheException, is CacheDataSink.CacheDataSinkException -> {
                InstreamAdPlayerError.Reason.CACHE_ERROR
            }
            else -> {
                InstreamAdPlayerError.Reason.UNKNOWN
            }
        }
    }

    private fun getReasonErrorInRendering(throwable: Throwable): InstreamAdPlayerError.Reason? {
        val cause = throwable.cause
        if (cause != null &&
            (cause.isMediaCodecException() || cause is IllegalStateException || cause is IllegalArgumentException)
        ) {
            val stackTrace = cause.stackTrace
            if (stackTrace.isNotEmpty() && stackTrace[0].isNativeMethod &&
                stackTrace[0].className == "android.media.MediaCodec"
            ) {
                return getReasonErrorInRenderingByMethodName(
                    stackTrace[0].methodName.orEmpty(),
                    cause
                )
            }
        }
        return null
    }

    private fun getReasonErrorInRenderingByMethodName(
        methodName: String,
        cause: Throwable,
    ): InstreamAdPlayerError.Reason? {
        return when {
            methodName == "native_dequeueOutputBuffer" -> {
                InstreamAdPlayerError.Reason.RENDERER_FAILED_DEQUEUE_OUTPUT_BUFFER
            }
            methodName == "native_dequeueInputBuffer" -> {
                InstreamAdPlayerError.Reason.RENDERER_FAILED_DEQUEUE_INPUT_BUFFER
            }
            methodName == "native_stop" -> {
                InstreamAdPlayerError.Reason.RENDERER_FAILED_STOP
            }
            methodName == "native_setSurface" -> {
                InstreamAdPlayerError.Reason.RENDERER_FAILED_SET_SURFACE
            }
            methodName == "releaseOutputBuffer" -> {
                InstreamAdPlayerError.Reason.RENDERER_FAILED_RELEASE_OUTPUT_BUFFER
            }
            methodName == "native_queueSecureInputBuffer" -> {
                InstreamAdPlayerError.Reason.RENDERER_FAILED_QUEUE_SECURE_INPUT_BUFFER
            }
            cause.isMediaCodecException() -> {
                InstreamAdPlayerError.Reason.RENDERER_MEDIA_CODEC_UNKNOWN
            }
            else -> null
        }
    }

    private fun Throwable.isMediaCodecException(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && this is MediaCodec.CodecException
    }

    private fun getReasonErrorDrmSession(exception: DrmSession.DrmSessionException): InstreamAdPlayerError.Reason {
        val cause = exception.cause ?: return InstreamAdPlayerError.Reason.DRM_SESSION_ERROR
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cause is MediaDrmResetException ||
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && cause is ResourceBusyException -> {
                InstreamAdPlayerError.Reason.DRM_MEDIA_RESOURCE_BUSY
            }
            cause is MediaCodec.CryptoException || cause is KeysExpiredException -> {
                InstreamAdPlayerError.Reason.DRM_KEYS_EXPIRED
            }
            else -> {
                InstreamAdPlayerError.Reason.DRM_SESSION_ERROR
            }
        }
    }

    private fun getReasonErrorHttp(
        exception: HttpDataSource.InvalidResponseCodeException
    ): InstreamAdPlayerError.Reason {
        return when (exception.responseCode) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> InstreamAdPlayerError.Reason.HTTP_CODE_UNAUTHORIZED
            HttpURLConnection.HTTP_FORBIDDEN -> InstreamAdPlayerError.Reason.HTTP_CODE_FORBIDDEN
            HttpURLConnection.HTTP_NOT_FOUND -> InstreamAdPlayerError.Reason.HTTP_CODE_NOT_FOUND
            else -> InstreamAdPlayerError.Reason.HTTP_CODE_UNKNOWN
        }
    }

    private fun getReasonErrorConnection(
        exception: HttpDataSource.HttpDataSourceException
    ): InstreamAdPlayerError.Reason {
        return if (exception.cause is SSLHandshakeException) {
            InstreamAdPlayerError.Reason.SSL_HANDSHAKE_ERROR
        } else {
            InstreamAdPlayerError.Reason.NETWORK_UNAVAILABLE
        }
    }
}