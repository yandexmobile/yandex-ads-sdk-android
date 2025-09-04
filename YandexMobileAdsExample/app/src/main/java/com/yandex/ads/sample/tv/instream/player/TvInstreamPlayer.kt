package com.yandex.ads.sample.tv.instream.player

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.yandex.ads.sample.R
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration
import com.yandex.mobile.ads.instream.media3.YandexAdsLoader
import com.yandex.mobile.ads.video.playback.VideoAdPlaybackListener
import com.yandex.mobile.ads.video.playback.model.VideoAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvInstreamPlayer(
    private val adUnitId: String,
    private val context: Context,
    private val playerView: PlayerView
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private var player: Player? = null
    private var playerListener: InstreamPlayerListener? = null
    private var yandexAdsLoader: YandexAdsLoader? = null
    private var tvAdPlaybackListener: TVAdPlaybackListener? = null

    var currentPosition by mutableLongStateOf(DEFAULT_CURRENT_PROGRESS)
        private set
    var duration by mutableLongStateOf(DEFAULT_DURATION)
        private set
    var isPlaying by mutableStateOf(false)
        private set
    var isShowingAd by mutableStateOf(false)
        private set

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    fun seekBack() {
        player?.seekTo((currentPosition - SEEK_DURATION).coerceAtLeast(0L))
    }

    fun seekForward() {
        player?.seekTo((currentPosition + SEEK_DURATION).coerceAtMost(duration))
    }

    fun requestAdFocus() {
        if (isShowingAd.not()) return
        playerView.post {
            playerView.adViewGroup.requestFocus()
        }
    }

    fun init() {
        initAdsLoader()
        initPlayer()
        attachListeners()
        startTimelineUpdater()
    }

    fun release() {
        stopTimelineUpdater()
        detachListener()
        releasePlayer()
        releaseAdsLoader()
    }

    private fun startTimelineUpdater() {
        scope.launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    player?.let { player ->
                        currentPosition = player.currentPosition
                        if (player.duration == C.TIME_UNSET) return@let
                        duration = player.duration
                    }
                }
                delay(PROGRESS_UPDATE_DELAY)
            }
        }
    }

    private fun stopTimelineUpdater() {
        scope.coroutineContext.cancelChildren()
    }

    private fun attachListeners() {
        playerListener = InstreamPlayerListener().also { listener ->
            player?.addListener(listener)
        }
        tvAdPlaybackListener = TVAdPlaybackListener().also { listener ->
            yandexAdsLoader?.setVideoAdPlaybackListener(listener)
        }
    }

    private fun detachListener() {
        playerListener?.let { listener ->
            player?.removeListener(listener)
        }
        playerListener = null
    }

    private fun initAdsLoader() {
        val configuration = InstreamAdRequestConfiguration
            .Builder(adUnitId)
            .build()
        yandexAdsLoader = YandexAdsLoader(context, configuration)
    }

    private fun initPlayer() {
        releasePlayer()
        player = createPlayer().also { player ->
            playerView.player = player
            playerView.useController = false
            yandexAdsLoader?.setPlayer(player)
        }
    }

    private fun createPlayer(): Player {
        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setLocalAdInsertionComponents({ yandexAdsLoader }, playerView)

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().apply {
                setMediaItem(getMediaItem())
                playWhenReady = true
                prepare()
            }
    }

    private fun getMediaItem(): MediaItem {
        val contentVideoUrl = context.getString(R.string.instream_content_url)
        val adTagUri = YandexAdsLoader.a.AD_TAG_URI.toUri()
        val mediaItem = MediaItem.Builder()
            .setUri(contentVideoUrl)
            .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(adTagUri).build())
            .build()
        return mediaItem
    }

    private fun releasePlayer() {
        yandexAdsLoader?.setPlayer(null)
        playerView.player = null
        player?.release()
        player = null
    }

    private fun releaseAdsLoader() {
        yandexAdsLoader?.release()
        yandexAdsLoader = null
    }

    private inner class InstreamPlayerListener : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            this@TvInstreamPlayer.isPlaying = isPlaying
        }

        override fun onPlayerError(error: PlaybackException) {
            Logger.error(context.getString(R.string.tv_error))
            super.onPlayerError(error)
        }
    }

    private inner class TVAdPlaybackListener : VideoAdPlaybackListener {
        override fun onAdClicked(videoAd: VideoAd) = Unit

        override fun onAdCompleted(videoAd: VideoAd) {
            playerView.useController = false
            isShowingAd = false
        }

        override fun onAdError(videoAd: VideoAd) {
            Logger.error(context.getString(R.string.tv_error))
        }

        override fun onAdPaused(videoAd: VideoAd) = Unit

        override fun onAdPrepared(videoAd: VideoAd) = Unit

        override fun onAdResumed(videoAd: VideoAd) = Unit

        override fun onAdSkipped(videoAd: VideoAd) {
            playerView.useController = false
            isShowingAd = false
        }

        override fun onAdStarted(videoAd: VideoAd) {
            if (isPlaying.not()) player?.play()
            playerView.useController = true
            isShowingAd = true
        }

        override fun onAdStopped(videoAd: VideoAd) = Unit

        override fun onImpression(videoAd: VideoAd) = Unit

        override fun onVolumeChanged(videoAd: VideoAd, volume: Float) = Unit
    }

    private companion object {
        private const val DEFAULT_CURRENT_PROGRESS = 0L
        private const val DEFAULT_DURATION = 0L
        private const val PROGRESS_UPDATE_DELAY = 200L
        private const val SEEK_DURATION = 5000L
    }
}
