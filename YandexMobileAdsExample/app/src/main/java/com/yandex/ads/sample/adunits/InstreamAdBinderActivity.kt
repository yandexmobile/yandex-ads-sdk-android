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
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.commit
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityInstreamAdBinderBinding
import com.yandex.ads.sample.player.SamplePlayer
import com.yandex.ads.sample.player.ad.SampleInstreamAdPlayer
import com.yandex.ads.sample.player.content.ContentVideoPlayer
import com.yandex.ads.sample.utils.applySystemBarsPadding
import com.yandex.mobile.ads.instream.InstreamAd
import com.yandex.mobile.ads.instream.InstreamAdBinder
import com.yandex.mobile.ads.instream.InstreamAdListener
import com.yandex.mobile.ads.instream.InstreamAdLoadListener
import com.yandex.mobile.ads.instream.InstreamAdLoader
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration

class InstreamAdBinderActivity : AppCompatActivity(R.layout.activity_instream_ad_binder) {

    private val adInfoFragment get() = requireNotNull(_adInfoFragment)
    private val eventLogger = InstreamAdEventLogger()

    private var instreamAdLoader: InstreamAdLoader? = null
    private var instreamAdBinder: InstreamAdBinder? = null
    private var activePlayer: SamplePlayer? = null
    private var instreamAdPlayer: SampleInstreamAdPlayer? = null
    private var contentVideoPlayer: ContentVideoPlayer? = null
    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivityInstreamAdBinderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInstreamAdBinderBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setContentView(binding.root)
        applySystemBarsPadding(findViewById(R.id.coordinatorLayout))

        _adInfoFragment = AdInfoFragment.newInstance(arrayListOf())
        adInfoFragment.onLoadClickListener = ::loadInstreamAd
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }
        initPlayer()
    }

    private fun initPlayer() {
        val contentStreamUrl = getString(R.string.instream_content_url)
        contentVideoPlayer = ContentVideoPlayer(contentStreamUrl, binding.exoPlayerView)
        instreamAdPlayer = SampleInstreamAdPlayer(binding.exoPlayerView)
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
        instreamAdBinder?.unbind()
        instreamAdBinder?.invalidateAdPlayer()
        instreamAdBinder?.invalidateVideoPlayer()

        contentVideoPlayer?.release()
        instreamAdPlayer?.release()
        activePlayer = null
        contentVideoPlayer = null
        instreamAdPlayer = null
        _adInfoFragment = null
        super.onDestroy()
    }

    private fun loadInstreamAd() {
        instreamAdLoader = InstreamAdLoader(this)
        instreamAdLoader?.setInstreamAdLoadListener(eventLogger)

        // Replace demo Page ID with actual Page ID
        val configuration = InstreamAdRequestConfiguration.Builder(PAGE_ID).build()
        instreamAdLoader?.loadInstreamAd(this, configuration)
    }

    private fun showInstreamAd(instreamAd: InstreamAd) {
        instreamAdBinder = InstreamAdBinder(
            this,
            instreamAd,
            checkNotNull(instreamAdPlayer),
            checkNotNull(contentVideoPlayer)
        )
        instreamAdBinder?.apply {
            setInstreamAdListener(eventLogger)
            bind(binding.instreamAdView)
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
