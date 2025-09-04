package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.tv.utils.formatTimestamp
import com.yandex.ads.sample.tv.theme.Typography.VideoInfoTextStyle
import com.yandex.ads.sample.tv.theme.Typography.VideoTitleTextStyle

@Composable
fun ProgressBarWithInfo(
    title: String,
    infoText: String,
    currentPosition: () -> Long,
    videoDuration: () -> Long,
    seekBack: () -> Unit,
    seekForward: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProgressText by remember {
        derivedStateOf { formatTimestamp(currentPosition()) }
    }
    val videoDurationText by remember {
        derivedStateOf { formatTimestamp(videoDuration()) }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    style = VideoTitleTextStyle
                )
                Text(
                    text = infoText,
                    color = Color.White,
                    style = VideoInfoTextStyle
                )
            }
            Text(
                text = "$currentProgressText/$videoDurationText",
                color = Color.White,
                style = VideoInfoTextStyle
            )
        }

        InstreamProgressIndicator(
            progress = {
                val videoDuration = videoDuration()
                val currentPosition = currentPosition()
                if (videoDuration <= 0L) 0f else currentPosition.toFloat() / videoDuration
            },
            seekBack = seekBack,
            seekForward = seekForward,
        )
    }
}
