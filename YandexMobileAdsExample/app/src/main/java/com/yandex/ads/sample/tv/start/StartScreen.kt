package com.yandex.ads.sample.tv.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.start.components.DemoButton
import com.yandex.ads.sample.tv.start.components.QRBlock
import com.yandex.ads.sample.tv.navigation.Route
import com.yandex.ads.sample.tv.start.components.InstreamFormatsBlock
import com.yandex.ads.sample.tv.theme.ColorScheme.Background

@Composable
fun StartScreen(
    navigate: (Route) -> Unit
) {
    val demoButtonFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        demoButtonFocusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Image(
            painter = painterResource(R.drawable.tv_start_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 64.dp, start = 128.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.tv_yandex_ads_logo),
                contentDescription = stringResource(R.string.tv_yandex_ads),
                modifier = Modifier.width(256.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            DemoButton(
                onClick = { navigate(Route.InstreamDemo) },
                modifier = Modifier.focusRequester(demoButtonFocusRequester)
            )
        }

        QRBlock(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 112.dp)
        )

        InstreamFormatsBlock(
            onOpenInstream = { route -> navigate(route) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 32.dp)
        )
    }
}
