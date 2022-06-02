/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.player.ad

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.yandex.ads.sample.yandex.instream.player.SamplePlayer
import com.yandex.mobile.ads.instream.player.ad.InstreamAdPlayer
import com.yandex.mobile.ads.instream.player.ad.InstreamAdPlayerListener
import com.yandex.mobile.ads.video.playback.model.VideoAd

class SampleInstreamAdPlayer(private val exoPlayerView: PlayerView) : InstreamAdPlayer, SamplePlayer {

    private val adPlayer = SimpleExoPlayer.Builder(exoPlayerView.context).build()
    private val exoPlayerErrorConverter = ExoPlayerErrorConverter()
    private var adPlayerListener: InstreamAdPlayerListener? = null
    private lateinit var videoAd: VideoAd

    init {
        adPlayer.addListener(AdPlayerEventListener())
    }

    override fun isPlaying() = adPlayer.isPlaying

    override fun onPause() {
        pause()
    }

    override fun onResume() {
        resume()
    }

    override fun prepareAd(videoAd: VideoAd) {
        this.videoAd = videoAd
        val mediaFile = videoAd.mediaFile
        val dataSourceFactory = DefaultDataSourceFactory(exoPlayerView.context, USER_AGENT)
        val adMediaItem = MediaItem.fromUri(mediaFile.url)
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(adMediaItem)
        adPlayer.apply {
            playWhenReady = false
            setMediaSource(videoSource, true)
            prepare()
        }
    }

    override fun playAd() {
        exoPlayerView.player = adPlayer
        exoPlayerView.useController = false
        adPlayer.playWhenReady = true
    }

    override fun pauseAd() {
        pause()
    }

    override fun resumeAd() {
        resume()
    }

    override fun stopAd() {
        adPlayer.playWhenReady = false
        adPlayerListener?.onAdStopped(videoAd)
    }

    override fun skipAd() {
        adPlayerListener?.onAdSkipped(videoAd)
    }

    override fun setVolume(volume: Float) {
        adPlayer.volume = volume
    }

    override fun release() {
        adPlayer.release()
    }

    override fun setInstreamAdPlayerListener(instreamAdPlayerListener: InstreamAdPlayerListener?) {
        adPlayerListener = instreamAdPlayerListener
    }

    override fun getAdDuration(): Long {
        return adPlayer.duration
    }

    override fun getAdPosition(): Long {
        return adPlayer.currentPosition
    }

    override fun isPlayingAd(): Boolean {
        return adPlayer.isPlaying
    }

    private fun pause() {
        if (adPlayer.isPlaying) {
            adPlayer.playWhenReady = false
        }
    }

    private fun resume() {
        if (!adPlayer.isPlaying) {
            adPlayer.playWhenReady = true
        }
    }

    fun onDestroy() {
        adPlayer.release()
    }

    private inner class AdPlayerEventListener : Player.Listener {

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
                    if (bufferingInProgress) {
                        onAdBufferingFinished()
                    }
                    if (adPrepared.not()) {
                        onAdPrepared()
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
            adStarted = false
            adPrepared = false;
            bufferingInProgress = false

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

        override fun onPlayerError(error: ExoPlaybackException) {
            adStarted = false
            adPrepared = false
            bufferingInProgress = false

            val adPlayerError = exoPlayerErrorConverter.convertExoPlayerError(error)
            adPlayerListener?.onError(videoAd, adPlayerError)
        }
    }

    private companion object {
        private const val USER_AGENT = "ad player"
    }
}
