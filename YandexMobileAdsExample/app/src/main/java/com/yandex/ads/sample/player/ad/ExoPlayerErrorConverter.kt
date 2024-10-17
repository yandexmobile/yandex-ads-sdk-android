/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.player.ad

import android.media.MediaCodec
import android.media.MediaDrmResetException
import android.media.ResourceBusyException
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.IllegalSeekPositionException
import androidx.media3.common.ParserException
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoTimeoutException
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.drm.DrmSession
import androidx.media3.exoplayer.drm.KeysExpiredException
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil
import androidx.media3.exoplayer.source.BehindLiveWindowException
import androidx.media3.exoplayer.upstream.Loader
import androidx.media3.exoplayer.video.MediaCodecVideoDecoderException
import androidx.media3.extractor.text.SubtitleDecoderException
import com.yandex.mobile.ads.instream.player.ad.error.InstreamAdPlayerError
import java.net.HttpURLConnection
import javax.net.ssl.SSLHandshakeException

@OptIn(UnstableApi::class)
class ExoPlayerErrorConverter {

    fun convertExoPlayerError(throwable: Throwable): InstreamAdPlayerError {
        val reason = getReason(throwable)
        return InstreamAdPlayerError(reason, throwable)
    }

    private fun getReason(throwable: Throwable): InstreamAdPlayerError.Reason {
        return when (throwable) {
            is ExoPlaybackException -> getExoPlaybackExceptionReason(throwable)
            is ExoTimeoutException -> InstreamAdPlayerError.Reason.TIMEOUT
            is IllegalSeekPositionException -> InstreamAdPlayerError.Reason.ILLEGAL_SEEK_POSITION
            is MediaCodecUtil.DecoderQueryException -> {
                InstreamAdPlayerError.Reason.DECODER_QUERY_ERROR
            }
            is MediaCodecRenderer.DecoderInitializationException -> {
                InstreamAdPlayerError.Reason.DECODER_INITIALIZATION_ERROR
            }
            is MediaCodecVideoDecoderException -> {
                getMediaCodecVideoDecoderExceptionReason(throwable)
            }
            is BehindLiveWindowException -> InstreamAdPlayerError.Reason.BEHIND_LIVE_WINDOW_ERROR
            is MediaCodec.CryptoException -> InstreamAdPlayerError.Reason.DRM_KEYS_EXPIRED
            is DrmSession.DrmSessionException -> getReasonErrorDrmSession(throwable)
            is HttpDataSource.CleartextNotPermittedException -> {
                InstreamAdPlayerError.Reason.HTTP_CLEARTEXT_NOT_PERMITTED
            }
            is HttpDataSource.InvalidResponseCodeException -> getReasonErrorHttp(throwable)
            is HttpDataSource.HttpDataSourceException -> getReasonErrorConnection(throwable)
            is ParserException -> InstreamAdPlayerError.Reason.CONTENT_PARSER_ERROR
            is Loader.UnexpectedLoaderException -> {
                InstreamAdPlayerError.Reason.LOADER_UNEXPECTED_ERROR
            }
            is AudioSink.ConfigurationException,
            is AudioSink.InitializationException,
            is DefaultAudioSink.InvalidAudioTrackTimestampException -> {
                InstreamAdPlayerError.Reason.AUDIO_ERROR
            }
            is SubtitleDecoderException -> InstreamAdPlayerError.Reason.SUBTITLE_ERROR
            is Cache.CacheException -> InstreamAdPlayerError.Reason.CACHE_ERROR
            else -> InstreamAdPlayerError.Reason.UNKNOWN
        }
    }

    private fun getReasonErrorInRendering(throwable: Throwable): InstreamAdPlayerError.Reason? {
        val cause = throwable.cause ?: return null
        if (cause !is MediaCodec.CodecException &&
            cause !is IllegalStateException &&
            cause !is IllegalArgumentException) return null

        val stackTrace = cause.stackTrace
        if (stackTrace.isNotEmpty() && stackTrace[0].isNativeMethod &&
            stackTrace[0].className == "android.media.MediaCodec"
        ) {
            return getReasonErrorInRenderingByMethodName(
                stackTrace[0].methodName.orEmpty(),
                cause
            )
        }
        return null
    }

    private fun getExoPlaybackExceptionReason(
        throwable: ExoPlaybackException,
    ): InstreamAdPlayerError.Reason {
        return getReasonErrorInRendering(throwable)
            ?: throwable.cause?.let { getReason(it) }
            ?: InstreamAdPlayerError.Reason.UNKNOWN
    }

    private fun getMediaCodecVideoDecoderExceptionReason(
        throwable: MediaCodecVideoDecoderException,
    ): InstreamAdPlayerError.Reason {
        return getReasonErrorInRendering(throwable)
            ?: InstreamAdPlayerError.Reason.DECODER_UNKNOWN_ERROR
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
            cause is MediaCodec.CodecException -> {
                InstreamAdPlayerError.Reason.RENDERER_MEDIA_CODEC_UNKNOWN
            }
            else -> null
        }
    }

    private fun getReasonErrorDrmSession(
        exception: DrmSession.DrmSessionException,
    ): InstreamAdPlayerError.Reason {
        val cause = exception.cause ?: return InstreamAdPlayerError.Reason.DRM_SESSION_ERROR
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                cause is MediaDrmResetException || cause is ResourceBusyException -> {
                InstreamAdPlayerError.Reason.DRM_MEDIA_RESOURCE_BUSY
            }
            cause is MediaCodec.CryptoException || cause is KeysExpiredException -> {
                InstreamAdPlayerError.Reason.DRM_KEYS_EXPIRED
            }
            else -> InstreamAdPlayerError.Reason.DRM_SESSION_ERROR
        }
    }

    private fun getReasonErrorHttp(
        exception: HttpDataSource.InvalidResponseCodeException
    ): InstreamAdPlayerError.Reason {
        return when (exception.responseCode) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                InstreamAdPlayerError.Reason.HTTP_CODE_UNAUTHORIZED
            }
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
