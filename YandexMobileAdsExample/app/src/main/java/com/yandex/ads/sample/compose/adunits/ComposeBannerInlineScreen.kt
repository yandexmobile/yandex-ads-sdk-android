/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2026 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose.adunits

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.compose.Banner
import com.yandex.mobile.ads.compose.BannerEvents
import com.yandex.mobile.ads.compose.BannerSize

private const val DEFAULT_BANNER_AD_UNIT_ID = "demo-banner-yandex"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeBannerInlineScreen(
    onNavigateBack: () -> Unit,
) {
    var adUnitId by remember { mutableStateOf(DEFAULT_BANNER_AD_UNIT_ID) }
    var logs by remember { mutableStateOf("") }

    fun appendLog(message: String) {
        logs = if (logs.isEmpty()) message else "$logs\n$message"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Banner (Inline)") },
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

            val logWindowScrollState = rememberScrollState()

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                    .verticalScroll(logWindowScrollState)
                    .padding(12.dp)
            ) {
                Text(text = logs, fontSize = 14.sp)
            }

            Banner(
                adRequest = AdRequest.Builder(adUnitId).build(),
                adSize = BannerSize.Inline(width = 320, maxHeight = 250),
                modifier = Modifier.fillMaxWidth().semantics { testTag = "banner" },
                events = BannerEvents(
                    onAdLoaded = { appendLog("onAdLoaded") },
                    onAdFailedToLoad = { error -> appendLog("onAdFailedToLoad: ${error.description}") },
                    onAdClicked = { appendLog("onAdClicked") },
                    onImpression = { appendLog("onImpression") },
                ),
            )
        }
    }
}
