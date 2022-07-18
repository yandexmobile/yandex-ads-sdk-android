/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.advanced

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityAdvancedInstreamYandexAdsBinding
import com.yandex.ads.sample.utils.Logger
import com.yandex.ads.sample.yandex.instream.advanced.player.SamplePlayer
import com.yandex.ads.sample.yandex.instream.advanced.player.ad.SampleInstreamAdPlayer
import com.yandex.ads.sample.yandex.instream.advanced.player.content.ContentVideoPlayer
import com.yandex.mobile.ads.instream.InstreamAd
import com.yandex.mobile.ads.instream.InstreamAdBinder
import com.yandex.mobile.ads.instream.InstreamAdListener
import com.yandex.mobile.ads.instream.InstreamAdLoadListener
import com.yandex.mobile.ads.instream.InstreamAdLoader
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration

class InstreamBinderYandexAdsActivity : AppCompatActivity() {

    private var instreamAdLoader: InstreamAdLoader? = null
    private var instreamAdBinder: InstreamAdBinder? = null
    private var activePlayer: SamplePlayer? = null
    private var instreamAdPlayer: SampleInstreamAdPlayer? = null
    private var contentVideoPlayer: ContentVideoPlayer? = null

    private lateinit var binding: ActivityAdvancedInstreamYandexAdsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedInstreamYandexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contentStreamUrl = getString(R.string.content_url_for_instream_ad)
        contentVideoPlayer = ContentVideoPlayer(contentStreamUrl, binding.exoPlayerView)
        instreamAdPlayer = SampleInstreamAdPlayer(binding.exoPlayerView)

        loadInstreamAd()
    }

    override fun onPause() {
        super.onPause()
        if (instreamAdPlayer?.isPlaying() == true || contentVideoPlayer?.isPlaying() == true) {
            activePlayer = if (contentVideoPlayer?.isPlaying() == true) contentVideoPlayer else instreamAdPlayer
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
        instreamAdBinder?.unbind()
        instreamAdBinder?.invalidateAdPlayer()
        instreamAdBinder?.invalidateVideoPlayer()

        contentVideoPlayer?.release()
        instreamAdPlayer?.release()
        activePlayer = null
        contentVideoPlayer = null
        instreamAdPlayer = null
        super.onDestroy()
    }

    private fun loadInstreamAd() {
        instreamAdLoader = InstreamAdLoader(this)
        instreamAdLoader?.setInstreamAdLoadListener(AdLoaderListener())

        // Replace demo Page ID with actual Page ID
        val configuration = InstreamAdRequestConfiguration.Builder(PAGE_ID).build()
        instreamAdLoader?.loadInstreamAd(this, configuration)
    }

    private fun showInstreamAd(instreamAd: InstreamAd) {
        instreamAdBinder = InstreamAdBinder(this, instreamAd, checkNotNull(instreamAdPlayer), checkNotNull(contentVideoPlayer))
        instreamAdBinder?.apply {
            setInstreamAdListener(InstreamAdPlaybackListener())
            bind(binding.instreamAdView)
        }
    }

    private inner class AdLoaderListener : InstreamAdLoadListener {
        override fun onInstreamAdLoaded(instreamAd: InstreamAd) {
            Logger.debug("onInstreamAdLoaded")
            showInstreamAd(instreamAd)
        }

        override fun onInstreamAdFailedToLoad(reason: String) {
            Logger.error(reason)
        }
    }

    private inner class InstreamAdPlaybackListener : InstreamAdListener {
        override fun onInstreamAdCompleted() {
            Logger.debug("Instream ad completed.")
        }

        override fun onInstreamAdPrepared() {
            Logger.debug("onInstreamAdPrepared")
        }

        override fun onError(reason: String) {
            Logger.error(reason)
        }
    }

    private companion object {

        const val PAGE_ID = "R-M-DEMO-instream-vmap"
    }
}
