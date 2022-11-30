/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityInstreamAdInrollBinding
import com.yandex.ads.sample.player.SamplePlayer
import com.yandex.ads.sample.player.ad.SampleInstreamAdPlayer
import com.yandex.ads.sample.player.content.ContentVideoPlayer
import com.yandex.ads.sample.player.content.ContentVideoPlayerListener
import com.yandex.ads.sample.utils.Logger
import com.yandex.mobile.ads.instream.InstreamAd
import com.yandex.mobile.ads.instream.InstreamAdBreakEventListener
import com.yandex.mobile.ads.instream.InstreamAdListener
import com.yandex.mobile.ads.instream.InstreamAdLoadListener
import com.yandex.mobile.ads.instream.InstreamAdLoader
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration
import com.yandex.mobile.ads.instream.inroll.Inroll
import com.yandex.mobile.ads.instream.inroll.InrollQueueProvider

class InstreamAdInrollActivity : AppCompatActivity(R.layout.activity_instream_ad_inroll) {

    private val adInfoFragment get() = _adInfoFragment!!
    private val eventLogger = InstreamAdEventLogger()

    private var instreamAdPlayer: SampleInstreamAdPlayer? = null
    private var contentVideoPlayer: ContentVideoPlayer? = null
    private var currentInroll: Inroll? = null
    private var activePlayer: SamplePlayer? = null
    private var isAdPlaying = false
    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivityInstreamAdInrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstreamAdInrollBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(arrayListOf())
        adInfoFragment.onLoadClickListener = ::loadInstreamAd
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        binding.playButton.setOnClickListener {
            if (isAdPlaying) pauseInroll() else resumeInroll()
        }
        initPlayer()
    }

    private fun initPlayer() {
        instreamAdPlayer = SampleInstreamAdPlayer(binding.exoPlayerView)
        contentVideoPlayer = ContentVideoPlayer(
            getString(R.string.instream_content_url),
            binding.exoPlayerView
        ).apply {
            setVideoPlayerListener(ContentVideoPlayerListener(this))
            prepareVideo()
        }
    }

    private fun loadInstreamAd() {
        val instreamAdLoader = InstreamAdLoader(this)
        instreamAdLoader.setInstreamAdLoadListener(eventLogger)

        // Replace demo Page ID with actual Page ID
        val configuration = InstreamAdRequestConfiguration.Builder(PAGE_ID).build()
        instreamAdLoader.loadInstreamAd(this, configuration)
    }

    private fun showInstreamAd(instreamAd: InstreamAd) {
        val inrollQueueProvider = InrollQueueProvider(this, instreamAd)
        val instreamAdBreakQueue = inrollQueueProvider.queue
        currentInroll = instreamAdBreakQueue.poll()?.apply {
            setListener(InrollListener())
            instreamAdPlayer?.let(::prepare)
            binding.playButton.apply {
                isEnabled = true
                setText(R.string.pause_inroll_button_text)
                setIconResource(R.drawable.ic_outline_pause_24)
                isAdPlaying = true
            }
        }
    }

    private fun pauseInroll() {
        currentInroll?.pause()

        binding.playButton.apply {
            setText(R.string.resume_inroll_button_text)
            setIconResource(R.drawable.ic_outline_play_arrow_24)
            isAdPlaying = false
        }
    }

    private fun resumeInroll() {
        currentInroll?.resume()

        binding.playButton.apply {
            setText(R.string.pause_inroll_button_text)
            setIconResource(R.drawable.ic_outline_pause_24)
            isAdPlaying = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (instreamAdPlayer?.isPlaying() == true || contentVideoPlayer?.isPlaying() == true) {
            activePlayer = contentVideoPlayer?.takeIf { it.isPlaying() } ?: instreamAdPlayer
            activePlayer?.pause()
        } else {
            activePlayer = null
        }
    }

    override fun onResume() {
        super.onResume()
        activePlayer?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        currentInroll?.apply {
            invalidate()
            setListener(null)
        }
        instreamAdPlayer?.release()
        contentVideoPlayer?.release()
        instreamAdPlayer = null
        contentVideoPlayer = null
        activePlayer = null
        currentInroll = null
        _adInfoFragment = null
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
            finishAdBreak()
        }

        override fun onInstreamAdBreakError(reason: String) {
            Logger.error(reason)
            finishAdBreak()
        }

        private fun finishAdBreak() {
            currentInroll = null
            contentVideoPlayer?.resumeVideo()
            isAdPlaying = false
            binding.playButton.isEnabled = false
        }
    }

    inner class InstreamAdEventLogger : InstreamAdLoadListener, InstreamAdListener {

        override fun onInstreamAdLoaded(ad: InstreamAd) {
            adInfoFragment.log("Instream ad loaded")
            adInfoFragment.hideLoading()
            showInstreamAd(ad)
        }

        override fun onInstreamAdFailedToLoad(error: String) {
            adInfoFragment.log("Instream ad failed to load: $error")
        }

        override fun onInstreamAdCompleted() {
            adInfoFragment.log("Instream ad completed")
        }

        override fun onInstreamAdPrepared() {
            adInfoFragment.log("Instream ad prepared")
        }

        override fun onError(error: String) {
            adInfoFragment.log("Instream ad error: $error")
        }
    }

    companion object {

        private const val PAGE_ID = "demo-instream-vmap-yandex"
    }
}
