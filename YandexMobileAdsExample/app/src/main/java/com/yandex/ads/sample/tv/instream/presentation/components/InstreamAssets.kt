package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.instream.player.TvInstreamPlayerState
import com.yandex.ads.sample.tv.instream.presentation.InstreamAction

@Composable
fun InstreamAssets(
    state: TvInstreamPlayerState,
    onAction: (InstreamAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.isShowingAd, state.isLoading) {
        if (state.isShowingAd.not() && state.isLoading.not()) {
            focusRequester.requestFocus()
        }
    }

    Box(modifier) {

        InstreamNavigationBlock(
            controlsEnabled = state.isShowingAd.not(),
            onExit = { onAction(InstreamAction.BackToMenu) },
            onBackToFormat = { onAction(InstreamAction.BackToFormat) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 32.dp)
        )

        if (state.isShowingAd.not()) {
            if (state.isLoading) {
                InstreamLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                PlayPauseButton(
                    isPlaying = state.isPlaying,
                    onPlay = { onAction(InstreamAction.Play) },
                    onPause = { onAction(InstreamAction.Pause) },
                    modifier = Modifier.align(Alignment.Center).focusRequester(focusRequester)
                )

                ProgressBarWithInfo(
                    title = stringResource(id = R.string.tv_video_name),
                    infoText = stringResource(id = R.string.tv_video_description),
                    currentPosition = { state.currentPosition },
                    videoDuration = { state.duration },
                    seekBack = { onAction(InstreamAction.SeekBack) },
                    seekForward = { onAction(InstreamAction.SeekForward) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.9f)
                                )
                            )
                        )
                        .padding(start = 32.dp, end = 32.dp, bottom = 24.dp, top = 48.dp)
                )
            }
        }
    }
}
