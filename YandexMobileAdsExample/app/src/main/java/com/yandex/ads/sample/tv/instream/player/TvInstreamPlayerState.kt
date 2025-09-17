package com.yandex.ads.sample.tv.instream.player

data class TvInstreamPlayerState(
    val currentPosition: Long = DEFAULT_CURRENT_PROGRESS,
    val duration: Long = DEFAULT_DURATION,
    val isPlaying: Boolean = false,
    val isShowingAd: Boolean = false,
    val isLoading: Boolean = true
) {
    companion object {
        private const val DEFAULT_CURRENT_PROGRESS = 0L
        private const val DEFAULT_DURATION = 0L
    }
}
