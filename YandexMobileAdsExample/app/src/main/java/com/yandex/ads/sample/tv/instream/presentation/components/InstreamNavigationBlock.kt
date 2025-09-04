package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R

@Composable
fun InstreamNavigationBlock(
    controlsEnabled: Boolean,
    onExit: () -> Unit,
    onBackToFormat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        InstreamButton(
            text = stringResource(R.string.tv_back_to_format),
            enabled = false,
            onClick = onBackToFormat,
            modifier = Modifier.width(268.dp)
        )
        InstreamButton(
            text = stringResource(R.string.tv_return_to_menu),
            enabled = controlsEnabled,
            onClick = onExit,
            modifier = Modifier.width(268.dp)
        )
    }
}
