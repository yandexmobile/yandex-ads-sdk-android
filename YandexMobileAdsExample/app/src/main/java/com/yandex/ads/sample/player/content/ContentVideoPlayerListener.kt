package com.yandex.ads.sample.player.content

import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener

class ContentVideoPlayerListener(
    private val contentVideoPlayer: ContentVideoPlayer,
) : VideoPlayerListener {

    override fun onVideoPrepared() {
        contentVideoPlayer.resumeVideo()
        Logger.debug("onVideoPrepared")
    }

    override fun onVideoCompleted() {
        Logger.debug("onVideoCompleted")
    }

    override fun onVideoPaused() {
        Logger.debug("onVideoPaused")
    }

    override fun onVideoError() {
        Logger.error("An error occurred during the video playing")
    }

    override fun onVideoResumed() {
        Logger.debug("onVideoResumed")
    }
}
