package com.yandex.ads.sample.tv.start.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yandex.ads.sample.R
import com.yandex.ads.sample.tv.theme.Typography.QRLabelTextStyle

@Composable
fun QRBlock(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.tv_qr_code),
            contentDescription = stringResource(R.string.tv_qr_code),
            modifier = Modifier
                .size(192.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.tv_scan_qr_text),
            style = QRLabelTextStyle,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(160.dp)
        )
    }
}
