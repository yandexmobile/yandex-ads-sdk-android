/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.player.content


import androidx.annotation.OptIn
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.yandex.ads.sample.player.SamplePlayer
import com.yandex.ads.sample.player.creator.MediaSourceCreator
import com.yandex.mobile.ads.instream.player.content.VideoPlayer
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener

@OptIn(UnstableApi::class)
class ContentVideoPlayer(
    private val videoUrl: String,
    private val exoPlayerView: PlayerView
) : VideoPlayer, SamplePlayer {

    private val context = exoPlayerView.context
    private val exoPlayer = ExoPlayer.Builder(context).build()
    private val mediaSourceCreator = MediaSourceCreator(context)

    private var videoPlayerListener: VideoPlayerListener? = null

    override val videoDuration: Long
        get() = exoPlayer.duration
    override val videoPosition: Long
        get() = exoPlayer.currentPosition
    override val volume: Float
        get() = exoPlayer.volume

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

    override fun pauseVideo() {
        exoPlayerView.useController = false
        pause()
    }

    override fun resumeVideo() {
        exoPlayerView.player = exoPlayer
        exoPlayerView.useController = true
        resume()
    }

    override fun setVideoPlayerListener(listener: VideoPlayerListener?) {
        videoPlayerListener = listener
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
