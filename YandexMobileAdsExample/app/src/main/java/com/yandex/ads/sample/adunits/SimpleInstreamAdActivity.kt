/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivitySimpleInstreamAdBinding
import com.yandex.ads.sample.network.Network
import com.yandex.ads.sample.utils.applySystemBarsPadding
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration
import com.yandex.mobile.ads.instream.media3.YandexAdsLoader

class SimpleInstreamAdActivity : AppCompatActivity(R.layout.activity_simple_instream_ad) {

    private val adInfoFragment get() = requireNotNull(_adInfoFragment)

    private var _adInfoFragment: AdInfoFragment? = null
    private var player: Player? = null

    private var wasPlaying = false
    private var rememberedPlayerPosition = 0L
    private var rememberedPlayWhenReady = false

    private lateinit var binding: ActivitySimpleInstreamAdBinding
    private lateinit var yandexAdsLoader: YandexAdsLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySimpleInstreamAdBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)
        applySystemBarsPadding(findViewById(R.id.coordinatorLayout))

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = ::loadInstream
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        initAdsLoader()
    }

    private fun initAdsLoader() {
        val configuration = InstreamAdRequestConfiguration
            .Builder(networks.first().adUnitId)
            .build()
        yandexAdsLoader = YandexAdsLoader(this, configuration)
    }

    private fun initPlayer() {
        player = createPlayer().also {
            binding.playerView.player = it
            yandexAdsLoader.setPlayer(it)

            // restore player if was playing before
            if (wasPlaying) {
                it.setMediaItem(getMediaItem())
                it.playWhenReady = rememberedPlayWhenReady
                it.seekTo(rememberedPlayerPosition)
                it.prepare()
            }
        }
    }

    private fun createPlayer(): Player {
        val mediaSourceFactory = DefaultMediaSourceFactory(this)
            .setLocalAdInsertionComponents({ yandexAdsLoader }, binding.playerView)

        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        return player
    }

    private fun loadInstream() {
        wasPlaying = true
        player?.apply {
            setMediaItem(getMediaItem())
            playWhenReady = true
            prepare()
        }
    }

    private fun getMediaItem(): MediaItem {
        val contentVideoUrl = getString(R.string.instream_content_url)
        val adTagUri = Uri.parse(YandexAdsLoader.AD_TAG_URI)
        val mediaItem = MediaItem.Builder()
            .setUri(contentVideoUrl)
            .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(adTagUri).build())
            .build()
        return mediaItem
    }


    private fun releasePlayer() {
        yandexAdsLoader.setPlayer(null)
        binding.playerView.player = null
        player?.also {
            rememberedPlayerPosition = it.contentPosition
            rememberedPlayWhenReady = it.playWhenReady
            it.release()
        }
        player = null
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT <= 23) return
        initPlayer()
        binding.playerView.onResume()
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT <= 23) return
        binding.playerView.onPause()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT > 23) return
        initPlayer()
        binding.playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT > 23) return
        binding.playerView.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        yandexAdsLoader.release()
        _adInfoFragment = null
        super.onDestroy()
    }

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-instream-vmap-yandex")
        )
    }
}
