/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.player.content

import android.content.Context
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.yandex.ads.sample.yandex.instream.player.SamplePlayer
import com.yandex.mobile.ads.instream.player.content.VideoPlayer
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener

class ContentVideoPlayer(
    private val videoUrl: String,
    private val exoPlayerView: PlayerView
) : VideoPlayer, SamplePlayer {

    private val exoPlayer = SimpleExoPlayer.Builder(exoPlayerView.context).build()
    private var videoPlayerListener: VideoPlayerListener? = null

    init {
        exoPlayer.addListener(ContentPlayerEventsListener())
    }

    override fun isPlaying() = exoPlayer.isPlaying

    override fun onResume() {
        exoPlayer.playWhenReady = true
    }

    override fun onPause() {
        exoPlayer.playWhenReady = false
    }

    override fun prepareVideo() {
        val videoSource = getMediaSource(exoPlayerView.context, videoUrl)
        exoPlayer.apply {
            playWhenReady = false
            addListener(ExoPlayerEventListener())
            setMediaSource(videoSource, true)
            prepare()
        }
    }

    private fun getMediaSource(context: Context, url: String): MediaSource {
        val userAgent = Util.getUserAgent(context, "Content video player")
        val dataSourceFactory = DefaultDataSourceFactory(context, userAgent)
        val mediaItem = MediaItem.fromUri(url)

        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }

    override fun getVideoPosition() = exoPlayer.currentPosition

    override fun getVideoDuration() = exoPlayer.duration

    override fun getVolume() = exoPlayer.volume

    override fun pauseVideo() {
        exoPlayer.playWhenReady = false
    }

    override fun resumeVideo() {
        exoPlayerView.player = exoPlayer
        exoPlayerView.useController = true
        exoPlayer.playWhenReady = true
    }

    override fun setVideoPlayerListener(playerListener: VideoPlayerListener?) {
        videoPlayerListener = playerListener
    }

    fun onDestroy() {
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

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                videoPlayerListener?.onVideoCompleted()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            videoPlayerListener?.onVideoError()
        }
    }

    private inner class ExoPlayerEventListener : Player.Listener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                videoPlayerListener?.onVideoPrepared()
                exoPlayer.removeListener(this)
            }
        }
    }
}