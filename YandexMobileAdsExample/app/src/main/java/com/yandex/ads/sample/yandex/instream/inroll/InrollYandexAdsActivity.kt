/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.inroll

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityInrollYandexAdsBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.ads.sample.yandex.instream.player.SamplePlayer
import com.yandex.ads.sample.yandex.instream.player.ad.SampleInstreamAdPlayer
import com.yandex.ads.sample.yandex.instream.player.content.ContentVideoPlayer
import com.yandex.mobile.ads.instream.InstreamAd
import com.yandex.mobile.ads.instream.InstreamAdBreakEventListener
import com.yandex.mobile.ads.instream.InstreamAdBreakQueue
import com.yandex.mobile.ads.instream.InstreamAdLoadListener
import com.yandex.mobile.ads.instream.InstreamAdLoader
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration
import com.yandex.mobile.ads.instream.inroll.Inroll
import com.yandex.mobile.ads.instream.inroll.InrollQueueProvider
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener

class InrollYandexAdsActivity : AppCompatActivity() {

    private var contentVideoPlayer: ContentVideoPlayer? = null
    private var instreamAdPlayer: SampleInstreamAdPlayer? = null
    private var currentInroll: Inroll? = null
    private var activePlayer: SamplePlayer? = null
    private lateinit var instreamAdBreakQueue: InstreamAdBreakQueue<Inroll>

    private lateinit var binding: ActivityInrollYandexAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInrollYandexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentVideoPlayer = ContentVideoPlayer(getString(R.string.content_url_for_instream_ad), binding.exoPlayerView)
        instreamAdPlayer = SampleInstreamAdPlayer(binding.exoPlayerView)

        binding.apply {
            loadAdButton.setOnClickListener(::loadInstreamAd)
            playInrollButton.setOnClickListener(::playInroll)
            resumeInrollButton.setOnClickListener(::resumeInroll)
            pauseInrollButton.setOnClickListener(::pauseInroll)
        }

        prepareVideo()
    }

    private fun loadInstreamAd(view: View) {
        val instreamAdLoader = InstreamAdLoader(this)
        instreamAdLoader.setInstreamAdLoadListener(AdLoaderListener())

        // Replace demo Page ID with actual Page ID
        val configuration = InstreamAdRequestConfiguration.Builder(PAGE_ID).build()
        instreamAdLoader.loadInstreamAd(this, configuration)

        binding.apply {
            pauseInrollButton.isEnabled = false
            resumeInrollButton.isEnabled = false
        }
    }

    private fun prepareVideo() {
        contentVideoPlayer?.apply {
            setVideoPlayerListener(ContentVideoPlayerListener())
            prepareVideo()
        }
    }

    private fun playInroll(view: View) {
        currentInroll = instreamAdBreakQueue.poll()
        currentInroll?.setListener(InrollListener())
        instreamAdPlayer?.let {
            currentInroll?.prepare(it)
        }

        binding.apply {
            loadAdButton.isEnabled = false
            playInrollButton.isEnabled = false
            pauseInrollButton.isEnabled = true
        }
    }

    private fun pauseInroll(view: View) {
        currentInroll?.pause()

        binding.apply {
            pauseInrollButton.isEnabled = false
            resumeInrollButton.isEnabled = true
        }
    }

    private fun resumeInroll(view: View) {
        currentInroll?.resume()

        binding.apply {
            pauseInrollButton.isEnabled = true
            resumeInrollButton.isEnabled = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (instreamAdPlayer?.isPlaying() == true || contentVideoPlayer?.isPlaying() == true) {
            activePlayer = if (contentVideoPlayer?.isPlaying() == true) contentVideoPlayer else instreamAdPlayer
            activePlayer?.onPause()
        } else {
            activePlayer = null
        }
    }

    override fun onResume() {
        super.onResume()
        activePlayer?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        currentInroll?.apply {
            invalidate()
            setListener(null)
        }
        instreamAdPlayer?.onDestroy()
        contentVideoPlayer?.onDestroy()
        instreamAdPlayer = null
        contentVideoPlayer = null
        activePlayer = null
        currentInroll = null
    }

    private inner class ContentVideoPlayerListener : VideoPlayerListener {

        override fun onVideoPrepared() {
            contentVideoPlayer?.resumeVideo()
            Logger.debug( "onVideoPrepared")
        }

        override fun onVideoCompleted() {
            Logger.debug( "onVideoCompleted")
        }

        override fun onVideoPaused() {
            Logger.debug( "onVideoPaused")
        }

        override fun onVideoError() {
            Logger.error("An error occurred during the video playing")
        }

        override fun onVideoResumed() {
            Logger.debug("onVideoResumed")
        }
    }

    private inner class AdLoaderListener : InstreamAdLoadListener {

        override fun onInstreamAdLoaded(instreamAd: InstreamAd) {
            Logger.debug("onInstreamAdLoaded")
            val inrollQueueProvider = InrollQueueProvider(this@InrollYandexAdsActivity, instreamAd)
            instreamAdBreakQueue = inrollQueueProvider.queue
            binding.playInrollButton.isEnabled = true
        }

        override fun onInstreamAdFailedToLoad(s: String) {
            Logger.error("Failed to load instream ad. Reason: $s")
        }
    }

    private inner class InrollListener : InstreamAdBreakEventListener {

        override fun onInstreamAdBreakPrepared() {
            Logger.debug("onInstreamAdBreakPrepared")
            currentInroll?.play(binding.instreamAdView)
        }

        override fun onInstreamAdBreakStarted() {
            Logger.debug("onInstreamAdBreakStarted")
            contentVideoPlayer?.pauseVideo()
        }

        override fun onInstreamAdBreakCompleted() {
            Logger.debug("onInstreamAdBreakCompleted")
            handleAdBreakCompleted()
        }

        override fun onInstreamAdBreakError(reason: String) {
            Logger.error(reason)
            handleAdBreakCompleted()
        }

        private fun handleAdBreakCompleted() {
            currentInroll = null
            contentVideoPlayer?.resumeVideo()

            binding.apply {
                loadAdButton.isEnabled = true
                playInrollButton.isEnabled = false
                pauseInrollButton.isEnabled = false
                resumeInrollButton.isEnabled = false
            }
        }
    }

    private companion object {

        const val PAGE_ID = "427408"
    }
}