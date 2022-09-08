/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivitySimpleInstreamYandexAdsBinding
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration
import com.yandex.mobile.ads.instream.exoplayer.YandexAdsLoader


class SimpleInstreamYandexAdsActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var yandexAdsLoader: YandexAdsLoader
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySimpleInstreamYandexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerView = binding.playerView
        yandexAdsLoader = createYandexAdsLoader()
        player = createContentPlayerWithAds(playerView, yandexAdsLoader)
        startVideoPlayback(player)
    }

    private fun createYandexAdsLoader(): YandexAdsLoader {
        val configuration = InstreamAdRequestConfiguration.Builder(DEMO_PAGE_ID).build()
        return YandexAdsLoader(this, configuration)
    }

    private fun createContentPlayerWithAds(
        playerView: PlayerView,
        yandexAdsLoader: YandexAdsLoader
    ): Player {
        val userAgent = Util.getUserAgent(this, getString(R.string.app_name))
        val dataSourceFactory = DefaultDataSourceFactory(this, userAgent)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider { yandexAdsLoader }
            .setAdViewProvider(playerView)

        val player = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory).build()
        playerView.player = player
        yandexAdsLoader.setPlayer(player)

        return player
    }

    private fun startVideoPlayback(player: Player) {
        val contentVideoUrl = getString(R.string.content_url_for_instream_ad)
        val mediaItem = MediaItem.fromUri(contentVideoUrl)

        player.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    private fun restorePlayer() {
        yandexAdsLoader.setPlayer(player)
        playerView.player = player
        if (player.isPlayingAd) {
            player.playWhenReady = true
        }
    }

    private fun releasePlayer() {
        yandexAdsLoader.setPlayer(null)
        playerView.player = null
        player.playWhenReady = false
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            restorePlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            restorePlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onDestroy() {
        yandexAdsLoader.release()
        player.release()

        super.onDestroy()
    }

    private companion object {
        const val DEMO_PAGE_ID = "R-M-DEMO-instream-vmap"
    }
}
