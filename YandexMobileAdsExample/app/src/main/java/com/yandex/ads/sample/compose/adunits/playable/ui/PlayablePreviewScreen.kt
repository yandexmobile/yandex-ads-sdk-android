/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2026 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose.adunits.playable.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yandex.ads.sample.R
import com.yandex.ads.sample.compose.adunits.playable.PlayableRoute
import com.yandex.ads.sample.compose.adunits.playable.model.PlayableItem
import com.yandex.ads.sample.compose.adunits.playable.viewmodel.PlayablePreviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayablePreviewScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
    viewModel: PlayablePreviewViewModel = viewModel(),
) {
    val history by viewModel.history.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()
    val withVideo by viewModel.withVideo.collectAsState()
    val adState by viewModel.adState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String?>("scanned_code", null)
            ?.collect { code ->
                if (code != null) {
                    navController.currentBackStackEntry?.savedStateHandle?.remove<String>("scanned_code")
                    viewModel.addItem(code)
                }
            }
    }

    LaunchedEffect(adState) {
        if (adState == PlayablePreviewViewModel.AdState.ReadyToPresent) {
            (context as? Activity)?.let { viewModel.showAd(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.playable_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (history.isEmpty()) {
                    EmptyState(modifier = Modifier.weight(1f))
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(history, key = { it.id }) { item ->
                            HistoryRow(
                                item = item,
                                isSelected = selectedItem?.id == item.id,
                                onTap = { viewModel.selectItem(item) },
                                onDelete = { viewModel.deleteItem(item.id) },
                            )
                        }
                    }
                }

                BottomBar(
                    withVideo = withVideo,
                    onToggleVideo = viewModel::toggleWithVideo,
                    onScanQr = {
                        runCatching { navController.navigate(PlayableRoute.QRScanner) }
                    },
                )
            }

            if (adState == PlayablePreviewViewModel.AdState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            if (adState is PlayablePreviewViewModel.AdState.Error) {
                val message = (adState as PlayablePreviewViewModel.AdState.Error).message
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 80.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(
    item: PlayableItem,
    isSelected: Boolean,
    onTap: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedBackground = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isSelected) selectedBackground else Color.Transparent)
            .clickable(onClick = onTap)
            .padding(start = 16.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.id,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            maxLines = 1,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        )
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qr_code_scanner),
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.playable_empty_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.playable_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun BottomBar(
    withVideo: Boolean,
    onToggleVideo: () -> Unit,
    onScanQr: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(checked = withVideo, onCheckedChange = { onToggleVideo() })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.playable_with_video), style = MaterialTheme.typography.bodyMedium)
        }
        Button(onClick = onScanQr) {
            Icon(
                painter = painterResource(R.drawable.ic_qr_code_scanner),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(stringResource(R.string.playable_scan_qr_button))
        }
    }
}
