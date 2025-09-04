package com.yandex.ads.sample.tv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yandex.ads.sample.tv.navigation.TvNavigation
import com.yandex.ads.sample.tv.theme.TVTheme

@Composable
fun ComposeApp() {
    TVTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TvNavigation()
        }
    }
}
