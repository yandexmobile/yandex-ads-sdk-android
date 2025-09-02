package com.yandex.ads.sample.tv.instream.player

import androidx.media3.ui.PlayerView

class PlayerViewAnimator(
    private val playerView: PlayerView,
    private val transitionDuration: Long = 700
) {

    fun fadeIn() {
        playerView.alpha = 0f
        playerView.animate()
            .alpha(1f)
            .setDuration(transitionDuration)
            .start()
    }

    fun fadeOut() {
        playerView.animate()
            .alpha(0f)
            .setDuration(transitionDuration)
            .start()
    }
}
