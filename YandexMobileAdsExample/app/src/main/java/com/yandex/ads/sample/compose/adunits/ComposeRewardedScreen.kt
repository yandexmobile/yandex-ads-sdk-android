/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2026 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose.adunits

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.compose.rememberRewardedAdLoader
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadResult
import kotlinx.coroutines.launch

private const val DEFAULT_REWARDED_AD_UNIT_ID = "demo-rewarded-yandex"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeRewardedScreen(
    onNavigateBack: () -> Unit,
) {
    var adUnitId by remember { mutableStateOf(DEFAULT_REWARDED_AD_UNIT_ID) }
    var logs by remember { mutableStateOf("") }
    var loadedAd by remember { mutableStateOf<RewardedAd?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun appendLog(message: String) {
        logs = if (logs.isEmpty()) message else "$logs\n$message"
    }

    val loader = rememberRewardedAdLoader()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Rewarded") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = adUnitId,
                onValueChange = { adUnitId = it },
                label = { Text("Ad Unit ID") },
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    loadedAd = null
                    appendLog("Loading…")
                    scope.launch {
                        when (val result = loader.loadAd(AdRequest.Builder(adUnitId).build())) {
                            is RewardedAdLoadResult.Success -> {
                                loadedAd = result.ad
                                appendLog("onAdLoaded")
                            }
                            is RewardedAdLoadResult.Failure ->
                                appendLog("onAdFailedToLoad: ${result.error.description}")
                        }
                    }
                }
            ) {
                Text("Load")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = loadedAd != null,
                onClick = {
                    loadedAd?.let { ad ->
                        ad.setAdEventListener(object : RewardedAdEventListener {
                            override fun onAdShown() { appendLog("onAdShown") }
                            override fun onAdFailedToShow(e: AdError) {
                                appendLog("onAdFailedToShow: ${e.description}")
                            }
                            override fun onAdDismissed() {
                                appendLog("onAdDismissed")
                                loadedAd = null
                            }
                            override fun onAdClicked() { appendLog("onAdClicked") }
                            override fun onAdImpression(data: ImpressionData?) {
                                appendLog("onAdImpression")
                            }
                            override fun onRewarded(reward: Reward) {
                                appendLog("onRewarded: type=${reward.type} amount=${reward.amount}")
                            }
                        })
                        ad.show(context as Activity)
                    }
                }
            ) {
                Text("Show")
            }

            val logWindowScrollState = rememberScrollState()

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                    .verticalScroll(logWindowScrollState)
                    .padding(12.dp)
            ) {
                Text(text = logs, fontSize = 14.sp)
            }
        }
    }
}
