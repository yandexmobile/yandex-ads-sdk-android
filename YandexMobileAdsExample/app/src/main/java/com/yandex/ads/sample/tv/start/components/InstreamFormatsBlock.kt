package com.yandex.ads.sample.tv.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.navigation.Route
import com.yandex.ads.sample.tv.theme.Typography.FormatsTextStyle
import com.yandex.ads.sample.tv.utils.BringPartiallyVisibleItemIntoViewWithPeek

@Composable
fun InstreamFormatsBlock(
    onOpenInstream: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatsFocusRequester = remember { FocusRequester() }
    val firstFormatFocusRequester = remember { FocusRequester() }
    var isFirstFormatsFocus by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .focusRequester(formatsFocusRequester)
            .focusProperties {
                onExit = { formatsFocusRequester.saveFocusedChild() }
                onEnter = {
                    if (isFirstFormatsFocus) {
                        firstFormatFocusRequester.requestFocus()
                        isFirstFormatsFocus = false
                    } else if (formatsFocusRequester.restoreFocusedChild()) {
                        cancelFocusChange()
                    }
                }
            }
    ) {
        Text(
            text = stringResource(R.string.tv_formats),
            style = FormatsTextStyle,
            color = Color.White,
            modifier = Modifier.padding(start = 32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        BringPartiallyVisibleItemIntoViewWithPeek {
            LazyRow(
                contentPadding = PaddingValues(start = 32.dp, end = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(134.dp)
                    .fillMaxWidth()
            ) {
                itemsIndexed(instreamFormats) { index, format ->

                    val formatModifier = if (index == 0) {
                        Modifier.focusRequester(firstFormatFocusRequester)
                    } else { Modifier }

                    InstreamFormatCard(
                        title = stringResource(format.titleRes),
                        imageResource = format.imageRes,
                        onClick = { onOpenInstream(format.route) },
                        modifier = formatModifier
                    )
                }
            }
        }
    }
}

private val instreamFormats = listOf(
    InstreamFormat(
        titleRes = R.string.tv_instream_preroll,
        imageRes = R.drawable.tv_preroll_preview,
        route = Route.InstreamPreroll
    ),
    InstreamFormat(
        titleRes = R.string.tv_instream_midroll,
        imageRes = R.drawable.tv_midroll_preview,
        route = Route.InstreamMidroll
    ),
    InstreamFormat(
        titleRes = R.string.tv_instream_postroll,
        imageRes = R.drawable.tv_postroll_preview,
        route = Route.InstreamPostroll
    ),
)

private data class InstreamFormat(
    val titleRes: Int,
    val imageRes: Int,
    val route: Route
)
