package com.yandex.ads.sample.tv.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yandex.ads.sample.R

object Typography {
    private val YSText = FontFamily(
        Font(R.font.ys_text_thin, FontWeight.Thin, FontStyle.Normal),
        Font(R.font.ys_text_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.ys_text_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.ys_text_regular_italic, FontWeight.Thin, FontStyle.Italic),
        Font(R.font.ys_text_medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.ys_text_bold, FontWeight.Bold, FontStyle.Normal)
    )

    val DemoButtonTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )

    val FormatsTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )

    val InstreamFormatNameTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )

    val QRLabelTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

    val VideoTitleTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )

    val VideoInfoTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )

    val InstreamButtonTextStyle = TextStyle(
        fontFamily = YSText,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )
}
