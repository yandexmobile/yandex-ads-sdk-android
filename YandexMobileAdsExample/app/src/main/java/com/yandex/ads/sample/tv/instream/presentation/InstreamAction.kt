package com.yandex.ads.sample.tv.instream.presentation

sealed interface InstreamAction {
    object BackToMenu : InstreamAction
    object BackToFormat : InstreamAction
    object Play : InstreamAction
    object Pause : InstreamAction
    object SeekBack : InstreamAction
    object SeekForward : InstreamAction
}
