/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivitySimpleInstreamAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration
import com.yandex.mobile.ads.instream.exoplayer.YandexAdsLoader

class SimpleInstreamAdActivity : AppCompatActivity(R.layout.activity_simple_instream_ad) {

    private val adInfoFragment get() = requireNotNull(_adInfoFragment)

    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivitySimpleInstreamAdBinding
    private lateinit var yandexAdsLoader: YandexAdsLoader
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleInstreamAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadInstream
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        initPlayer()
    }

    private fun initPlayer() {
        val configuration = InstreamAdRequestConfiguration
            .Builder(networks.first().adUnitId)
            .build()
        yandexAdsLoader = YandexAdsLoader(this, configuration)
        player = createPlayer()
    }

    private fun createPlayer(): Player {
        val userAgent = Util.getUserAgent(this, getString(R.string.app_name))
        val dataSourceFactory = DefaultDataSourceFactory(this, userAgent)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider { yandexAdsLoader }
            .setAdViewProvider(binding.playerView)

        val player = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
        binding.playerView.player = player
        yandexAdsLoader.setPlayer(player)

        return player
    }

    private fun loadInstream() {
        val contentVideoUrl = getString(R.string.instream_content_url)
        val mediaItem = MediaItem.Builder()
            .setUri(contentVideoUrl)
            .setAdTagUri(YandexAdsLoader.AD_TAG_URI).build()

        player.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    private fun restorePlayer() {
        yandexAdsLoader.setPlayer(player)
        binding.playerView.player = player
        if (player.isPlayingAd) {
            player.playWhenReady = true
        }
    }

    private fun releasePlayer() {
        yandexAdsLoader.setPlayer(null)
        binding.playerView.player = null
        player.playWhenReady = false
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT <= Build.VERSION_CODES.M) return
        restorePlayer()
        binding.playerView.onResume()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT <= Build.VERSION_CODES.M) return
        binding.playerView.onPause()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT > Build.VERSION_CODES.M) return
        restorePlayer()
        binding.playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT > Build.VERSION_CODES.M) return
        binding.playerView.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        yandexAdsLoader.release()
        player.release()
        _adInfoFragment = null
        super.onDestroy()
    }

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-instream-vmap-yandex")
        )
    }
}
