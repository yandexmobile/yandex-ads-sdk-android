/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.player.ad

import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.yandex.ads.sample.player.creator.MediaSourceCreator
import com.yandex.mobile.ads.instream.player.ad.InstreamAdPlayerListener
import com.yandex.mobile.ads.video.playback.model.VideoAd

class SampleVideoAdPlayer(
    private val videoAd: VideoAd,
    private val exoPlayerView: PlayerView,
) {

    private val context = exoPlayerView.context
    private val exoPlayerErrorConverter = ExoPlayerErrorConverter()
    private val mediaSourceCreator = MediaSourceCreator(context)

    private val adPlayer = SimpleExoPlayer.Builder(context).build().apply {
        addListener(ExoPlayerEventsListener())
    }

    private var adPlayerListener: InstreamAdPlayerListener? = null

    val adDuration: Long
        get() = adPlayer.duration

    val adPosition: Long
        get() = adPlayer.currentPosition

    val isPlayingAd: Boolean
        get() = adPlayer.isPlaying

    fun prepareAd() {
        val streamUrl = videoAd.mediaFile.url
        val mediaSource = mediaSourceCreator.createMediaSource(streamUrl)
        adPlayer.apply {
            playWhenReady = false
            setMediaSource(mediaSource)
            prepare()
        }
    }

    fun playAd() {
        exoPlayerView.player = adPlayer
        resumeAd()
    }

    fun pauseAd() {
        adPlayer.playWhenReady = false
    }

    fun resumeAd() {
        adPlayer.playWhenReady = true
    }

    fun stopAd() {
        pauseAd()
        adPlayerListener?.onAdStopped(videoAd)
    }

    fun skipAd() {
        pauseAd()
        adPlayerListener?.onAdSkipped(videoAd)
    }

    fun setVolume(volume: Float) {
        adPlayer.volume = volume
    }

    fun getVolume() = adPlayer.volume


    fun releaseAd() {
        adPlayer.release()
    }

    fun setInstreamAdPlayerListener(instreamAdPlayerListener: InstreamAdPlayerListener?) {
        adPlayerListener = instreamAdPlayerListener
    }

    fun onDestroy() {
        adPlayer.release()
    }

    private inner class ExoPlayerEventsListener : Player.Listener {

        private var adStarted = false
        private var adPrepared = false
        private var bufferingInProgress = false

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                onResumePlayback()
            } else {
                onPausePlayback()
            }
        }

        private fun onResumePlayback() {
            if (adStarted) {
                adPlayerListener?.onAdResumed(videoAd)
            } else {
                adPlayerListener?.onAdStarted(videoAd)
            }
            adStarted = true
        }

        private fun onPausePlayback() {
            adPlayerListener?.onAdPaused(videoAd)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    if (adPrepared.not()) {
                        onAdPrepared()
                    }
                    if (bufferingInProgress) {
                        onAdBufferingFinished()
                    }
                }
                Player.STATE_BUFFERING -> onAdBufferingStarted()
                Player.STATE_ENDED -> onEndedState()
            }
        }

        private fun onAdPrepared() {
            adPlayerListener?.onAdPrepared(videoAd)
        }

        private fun onEndedState() {
            resetState()
            adPlayerListener?.onAdCompleted(videoAd)
        }

        private fun onAdBufferingStarted() {
            bufferingInProgress = true
            adPlayerListener?.onAdBufferingStarted(videoAd)
        }

        private fun onAdBufferingFinished() {
            bufferingInProgress = false
            adPlayerListener?.onAdBufferingFinished(videoAd)
        }

        override fun onPlayerError(error: PlaybackException) {
            resetState()
            val adPlayerError = exoPlayerErrorConverter.convertExoPlayerError(error)
            adPlayerListener?.onError(videoAd, adPlayerError)
        }

        fun resetState() {
            adStarted = false
            adPrepared = false
            bufferingInProgress = false
        }
    }
}
