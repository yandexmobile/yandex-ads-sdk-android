/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.advanced.player.content

import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.yandex.ads.sample.yandex.instream.advanced.player.SamplePlayer
import com.yandex.ads.sample.yandex.instream.advanced.player.creator.MediaSourceCreator
import com.yandex.mobile.ads.instream.player.content.VideoPlayer
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener

class ContentVideoPlayer(
    private val videoUrl: String,
    private val exoPlayerView: PlayerView
) : VideoPlayer, SamplePlayer {

    private val context = exoPlayerView.context
    private val exoPlayer = SimpleExoPlayer.Builder(context).build()
    private val mediaSourceCreator = MediaSourceCreator(context)

    private var videoPlayerListener: VideoPlayerListener? = null

    init {
        exoPlayer.addListener(ContentPlayerEventsListener())
    }

    override fun isPlaying() = exoPlayer.isPlaying

    override fun resume() {
        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
    }

    override fun prepareVideo() {
        val mediaSource = mediaSourceCreator.createMediaSource(videoUrl)
        exoPlayer.apply {
            playWhenReady = false
            addListener(ContentPlayerPrepareListener())
            setMediaSource(mediaSource)
            prepare()
        }
    }

    override fun getVideoPosition() = exoPlayer.currentPosition

    override fun getVideoDuration() = exoPlayer.duration

    override fun getVolume() = exoPlayer.volume

    override fun pauseVideo() {
        exoPlayerView.useController = false
        pause()
    }

    override fun resumeVideo() {
        exoPlayerView.player = exoPlayer
        exoPlayerView.useController = true
        resume()
    }

    override fun setVideoPlayerListener(playerListener: VideoPlayerListener?) {
        videoPlayerListener = playerListener
    }

    fun release() {
        exoPlayer.release()
    }

    private inner class ContentPlayerEventsListener : Player.Listener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                videoPlayerListener?.onVideoResumed()
            } else {
                videoPlayerListener?.onVideoPaused()
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                onVideoCompleted()
            }
        }

        private fun onVideoCompleted() {
            exoPlayerView.useController = false
            videoPlayerListener?.onVideoCompleted()
        }

        override fun onPlayerError(error: PlaybackException) {
            videoPlayerListener?.onVideoError()
        }
    }

    private inner class ContentPlayerPrepareListener : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                videoPlayerListener?.onVideoPrepared()
                exoPlayer.removeListener(this)
            }
        }
    }
}
