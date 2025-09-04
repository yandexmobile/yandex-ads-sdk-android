package com.yandex.ads.sample.tv.instream.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.instream.player.PlayerViewAnimator
import com.yandex.ads.sample.tv.instream.player.TvInstreamPlayer
import com.yandex.ads.sample.tv.instream.presentation.components.InstreamNavigationBlock
import com.yandex.ads.sample.tv.instream.presentation.components.PlayPauseButton
import com.yandex.ads.sample.tv.instream.presentation.components.ProgressBarWithInfo
import com.yandex.ads.sample.tv.theme.ColorScheme.Background

@Composable
fun InstreamScreen(
    adUnitId: String,
    onExit: () -> Unit
) {
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val playerView = remember { PlayerView(context) }
    val playerViewAnimator = remember { PlayerViewAnimator(playerView) }
    val player = remember { TvInstreamPlayer(adUnitId, context, playerView) }
    var wasPlayingAdBeforeRecomposition by remember { mutableStateOf(false) }

    fun onBack() {
        playerViewAnimator.fadeOut()
        onExit()
    }

    DisposableEffect(player) {
        player.init()
        playerViewAnimator.fadeIn()
        onDispose {
            player.release()
        }
    }

    SideEffect {
        if (player.isShowingAd) {
            player.requestAdFocus()
            wasPlayingAdBeforeRecomposition = true
        } else if (wasPlayingAdBeforeRecomposition) {
            focusRequester.requestFocus()
            wasPlayingAdBeforeRecomposition = false
        }
    }

    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { playerView },
        )

        InstreamNavigationBlock(
            controlsEnabled = player.isShowingAd.not(),
            onExit = { onBack() },
            onBackToFormat = { player.requestAdFocus() },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 32.dp)
        )

        if (player.isShowingAd.not()) {
            PlayPauseButton(
                isPlaying = player.isPlaying,
                onPlay = { player.play() },
                onPause = { player.pause() },
                modifier = Modifier.align(Alignment.Center).focusRequester(focusRequester)
            )

            ProgressBarWithInfo(
                title = stringResource(id = R.string.tv_video_name),
                infoText = stringResource(id = R.string.tv_video_description),
                currentPosition = { player.currentPosition },
                videoDuration = { player.duration },
                seekBack = { player.seekBack() },
                seekForward = { player.seekForward() },
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
