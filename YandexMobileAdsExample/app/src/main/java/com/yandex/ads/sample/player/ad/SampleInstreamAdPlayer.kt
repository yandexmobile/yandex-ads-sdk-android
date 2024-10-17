/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.player.ad

import androidx.media3.ui.PlayerView
import com.yandex.ads.sample.player.SamplePlayer
import com.yandex.mobile.ads.instream.player.ad.InstreamAdPlayer
import com.yandex.mobile.ads.instream.player.ad.InstreamAdPlayerListener
import com.yandex.mobile.ads.video.playback.model.VideoAd

class SampleInstreamAdPlayer(
    private val exoPlayerView: PlayerView,
) : InstreamAdPlayer, SamplePlayer {

    private val adPlayers = mutableMapOf<VideoAd, SampleVideoAdPlayer>()

    private var currentVideoAd: VideoAd? = null
    private var adPlayerListener: InstreamAdPlayerListener? = null

    override fun setInstreamAdPlayerListener(instreamAdPlayerListener: InstreamAdPlayerListener?) {
        adPlayerListener = instreamAdPlayerListener
        adPlayers.values.forEach { it.setInstreamAdPlayerListener(instreamAdPlayerListener) }
    }

    override fun prepareAd(videoAd: VideoAd) {
        val videoAdPlayer = SampleVideoAdPlayer(videoAd, exoPlayerView)
        adPlayers[videoAd] = videoAdPlayer

        videoAdPlayer.setInstreamAdPlayerListener(adPlayerListener)
        videoAdPlayer.prepareAd()
    }

    override fun playAd(videoAd: VideoAd) {
        currentVideoAd = videoAd
        adPlayers[videoAd]?.playAd()
    }

    override fun pauseAd(videoAd: VideoAd) {
        adPlayers[videoAd]?.pauseAd()
    }

    override fun resumeAd(videoAd: VideoAd) {
        adPlayers[videoAd]?.resumeAd()
    }

    override fun stopAd(videoAd: VideoAd) {
        adPlayers[videoAd]?.stopAd()
    }

    override fun skipAd(videoAd: VideoAd) {
        adPlayers[videoAd]?.skipAd()
    }

    override fun setVolume(videoAd: VideoAd, volume: Float) {
        adPlayers[videoAd]?.setVolume(volume)
    }

    override fun getVolume(videoAd: VideoAd): Float {
        return adPlayers[videoAd]?.getVolume() ?: DEFAULT_VOLUME
    }

    override fun getAdDuration(videoAd: VideoAd): Long {
        return adPlayers[videoAd]?.adDuration ?: 0
    }

    override fun getAdPosition(videoAd: VideoAd): Long {
        return adPlayers[videoAd]?.adPosition ?: 0
    }

    override fun isPlayingAd(videoAd: VideoAd): Boolean {
        return adPlayers[videoAd]?.isPlayingAd ?: false
    }

    override fun releaseAd(videoAd: VideoAd) {
        if(videoAd == currentVideoAd) {
            currentVideoAd = null
        }

        adPlayers[videoAd]?.let { videoAdPlayer ->
            videoAdPlayer.setInstreamAdPlayerListener(null)
            videoAdPlayer.releaseAd()
        }

        adPlayers.remove(videoAd)
    }

    fun release() {
        adPlayers.values.forEach(SampleVideoAdPlayer::releaseAd)
        currentVideoAd = null
    }

    override fun isPlaying(): Boolean {
        return adPlayers[currentVideoAd]?.isPlayingAd ?: false
    }

    override fun resume() {
        adPlayers[currentVideoAd]?.resumeAd()
    }

    override fun pause() {
        adPlayers[currentVideoAd]?.pauseAd()
    }

    private companion object {
        private const val DEFAULT_VOLUME = 0f
    }
}
