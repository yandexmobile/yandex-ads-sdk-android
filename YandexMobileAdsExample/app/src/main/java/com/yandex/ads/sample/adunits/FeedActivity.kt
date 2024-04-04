/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2024 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adunits

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.yandex.ads.sample.R
import com.yandex.ads.sample.databinding.ActivityFeedBinding
import com.yandex.ads.sample.feed.ScreenContentDataAdapter
import com.yandex.ads.sample.feed.LogsDialog
import com.yandex.ads.sample.utils.Logger
import com.yandex.ads.sample.utils.ScreenUtil.screenWidth
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.feed.FeedAd
import com.yandex.mobile.ads.feed.FeedAdAdapter
import com.yandex.mobile.ads.feed.FeedAdAppearance
import com.yandex.mobile.ads.feed.FeedAdEventListener
import com.yandex.mobile.ads.feed.FeedAdLoadListener
import com.yandex.mobile.ads.feed.FeedAdRequestConfiguration
import java.lang.StringBuilder

class FeedActivity: AppCompatActivity() {

    private val eventLogger = FeedAdEventLogger()
    private val loadLogger = FeedAdLoadLogger()

    private val logs: StringBuilder = StringBuilder()

    private var feedAdAdapter: FeedAdAdapter? = null

    private var screenContentDataAdapter: ScreenContentDataAdapter? = null
    private var concatAdapter: ConcatAdapter? = null

    private lateinit var binding: ActivityFeedBinding
    private lateinit var feedAd: FeedAd
    private lateinit var logsDialog: LogsDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        binding.setupUiBinding()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            logs.clear()
            onBackPressedDispatcher.onBackPressed()
        }

        logsDialog = LogsDialog(this)

        setupFeedAd()
    }

    private fun setupFeedAd() {
        val calculatedFeedCardWidth = calculateFeedCardWidth()
        val feedAdAppearance = FeedAdAppearance(
            cardWidth = calculatedFeedCardWidth,
            cardCornerRadius = CARD_CORNER_RADIUS_DP
        )
        val feedAdRequestConfiguration = FeedAdRequestConfiguration.Builder(AD_UNIT_ID).build()

        feedAd = FeedAd.Builder(this, feedAdRequestConfiguration, feedAdAppearance).build()
        feedAd.loadListener = loadLogger
        feedAd.preloadAd()
    }

    private fun showFeedAdStandalone() {
        logs.clear()

        feedAdAdapter = FeedAdAdapter(feedAd)
        feedAdAdapter?.eventListener = eventLogger

        binding.feedRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.feedRecyclerView.adapter = feedAdAdapter
    }

    private fun showFeedAdWithScreenContent() {
        logs.clear()

        feedAdAdapter = FeedAdAdapter(feedAd)
        feedAdAdapter?.eventListener = eventLogger
        screenContentDataAdapter = ScreenContentDataAdapter(List(SCREEN_CONTENT_ITEMS_COUNT) {
            position -> position + 1
        })

        concatAdapter = ConcatAdapter(listOf(screenContentDataAdapter, feedAdAdapter))

        binding.feedRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.feedRecyclerView.adapter = concatAdapter
    }

    private fun ActivityFeedBinding.setupUiBinding() {
        showAdButton.setOnClickListener { showFeedAdStandalone() }
        showConcatAdButton.setOnClickListener { showFeedAdWithScreenContent() }
    }

    private fun calculateFeedCardWidth(): Int {
        return screenWidth - 2 * CARD_WIDTH_MARGIN_DP
    }

    private inner class FeedAdLoadLogger : FeedAdLoadListener {

        override fun onAdFailedToLoad(error: AdRequestError) {
            val message = "Feed on ad failed to load: $error"
            log(message)
        }

        override fun onAdLoaded() {
            val message = "Feed on ad loaded"
            log(message)
        }
    }

    private inner class FeedAdEventLogger : FeedAdEventListener {

        override fun onAdClicked() {
            val message = "Feed on ad clicked"
            log(message)
        }

        override fun onImpression(impressionData: ImpressionData?) {
            val message = "Feed on impression: $impressionData"
            log(message)
        }
    }

    private fun log(message: String) {
        logs.append("$message\n")
        logsDialog.setLogs(logs.toString())
        Logger.debug(message)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_button -> {
                logsDialog.show(logs.toString())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private companion object {

        private const val AD_UNIT_ID = "demo-feed-yandex"
        private const val CARD_WIDTH_MARGIN_DP = 24
        private const val CARD_CORNER_RADIUS_DP = 14.0
        private const val SCREEN_CONTENT_ITEMS_COUNT = 15
    }
}
