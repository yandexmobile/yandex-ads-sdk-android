package com.yandex.ads.sample.tv.instream.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.instream.player.model.ErrorType
import com.yandex.ads.sample.tv.theme.ColorScheme.Background
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamErrorContainer
import com.yandex.ads.sample.tv.theme.ColorScheme.InstreamErrorIcon
import com.yandex.ads.sample.tv.theme.Typography.InstreamErrorDescriptionStyle
import com.yandex.ads.sample.tv.theme.Typography.InstreamErrorHeaderStyle

@Composable
fun InstreamErrorScreen(
    error: ErrorType,
    onTryAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.width(448.dp).align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(R.drawable.tv_error),
                tint = InstreamErrorIcon,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(InstreamErrorContainer)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(error.title),
                    color = Color.White,
                    style = InstreamErrorHeaderStyle
                )
                Text(
                    text = stringResource(error.description),
                    color = Color.White.copy(alpha = 0.8f),
                    style = InstreamErrorDescriptionStyle
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InstreamButton(
                    text = stringResource(R.string.tv_error_try_again),
                    onClick = onTryAgain,
                    modifier = Modifier.weight(1f).focusRequester(focusRequester)
                )
                InstreamButton(
                    text = stringResource(R.string.tv_error_return_to_menu),
                    onClick = onBackToMenu,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
