/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R

data class ComposeExample(
    val iconId: Int,
    val titleId: Int,
    val route: ComposeRoute
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeHomeScreen(
    navigate: (ComposeRoute) -> Unit,
    onNavigateBack: () -> Unit
) {
    val examples = listOf(
        ComposeExample(
            iconId = R.drawable.ic_outline_ad_units_24,
            titleId = R.string.sticky_banner_title,
            route = ComposeRoute.BannerSticky
        ),
        ComposeExample(
            iconId = R.drawable.ic_outline_aspect_ratio_24,
            titleId = R.string.inline_banner_title,
            route = ComposeRoute.BannerInline
        ),
        ComposeExample(
            iconId = R.drawable.ic_outline_fullscreen_24,
            titleId = R.string.interstitial_title,
            route = ComposeRoute.Interstitial
        ),
        ComposeExample(
            iconId = R.drawable.ic_outline_video_library_24,
            titleId = R.string.rewarded_title,
            route = ComposeRoute.Rewarded
        ),
        ComposeExample(
            iconId = R.drawable.ic_full_coverage_24,
            titleId = R.string.appopenad_title,
            route = ComposeRoute.AppOpenAd
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jetpack Compose Examples") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(examples) { example ->
                ExampleCard(
                    example = example,
                    onClick = { navigate(example.route) }
                )
            }
        }
    }
}

@Composable
fun ExampleCard(
    example: ComposeExample,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = example.iconId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = stringResource(id = example.titleId),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_outline_chevron_right_24),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
