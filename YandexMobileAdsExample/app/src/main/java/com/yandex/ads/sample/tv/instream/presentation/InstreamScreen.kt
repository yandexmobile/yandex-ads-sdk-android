package com.yandex.ads.sample.tv.instream.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.ui.PlayerView
import com.yandex.ads.sample.tv.instream.player.PlayerViewAnimator
import com.yandex.ads.sample.tv.instream.player.TvInstreamPlayer
import com.yandex.ads.sample.tv.instream.presentation.components.InstreamAssets
import com.yandex.ads.sample.tv.theme.ColorScheme.Background

@Composable
fun InstreamScreen(
    adUnitId: String,
    onExit: () -> Unit
) {
    val context = LocalContext.current

    val playerView = remember { PlayerView(context) }
    val playerViewAnimator = remember { PlayerViewAnimator(playerView) }
    val player = remember { TvInstreamPlayer(adUnitId, context, playerView) }
    val state by player.state.collectAsStateWithLifecycle()

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

    BackHandler { onBack() }

    AndroidView(
        modifier = Modifier.fillMaxSize().background(Background),
        factory = { playerView },
    )

    InstreamAssets(
        state = state,
        onAction = { action ->
            when (action) {
                InstreamAction.Play -> player.play()
                InstreamAction.Pause -> player.pause()
                InstreamAction.SeekBack -> player.seekBack()
                InstreamAction.SeekForward -> player.seekForward()
                InstreamAction.BackToMenu -> onBack()
                InstreamAction.BackToFormat -> player.requestAdFocus()
                InstreamAction.TryAgain -> player.restart()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
