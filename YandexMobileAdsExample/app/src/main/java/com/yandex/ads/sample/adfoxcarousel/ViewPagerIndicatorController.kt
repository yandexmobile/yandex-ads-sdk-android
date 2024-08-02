/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfoxcarousel

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import com.yandex.ads.sample.R
import kotlin.math.absoluteValue

class ViewPagerIndicatorController(private val indicatorLayout: LinearLayout) {
    private val dotsImageViews = arrayListOf<ImageView>()

    fun setupIndicator(pagesCount: Int, selectedPage: Int) {
        indicatorLayout.removeAllViews()
        dotsImageViews.clear()

        for (i in 0 until pagesCount) {
            val dotView = ImageView(indicatorLayout.context).apply {
                setImageDrawable(ContextCompat.getDrawable(indicatorLayout.context, R.drawable.shape_adfox_carousel_indicator_dot))
            }
            dotsImageViews.add(dotView)
            indicatorLayout.addView(dotsImageViews[i])
        }
        changeSelectedPage(selectedPage)
    }

    fun changeSelectedPage(selectedPage: Int) {
        dotsImageViews.forEachIndexed { page, view ->
            val distance = (selectedPage - page).absoluteValue
            val size = when {
                dotsImageViews.size <= 4 || distance < 2 -> R.dimen.adfox_carousel_indicator_dot_size_large
                distance == 2 -> R.dimen.adfox_carousel_indicator_dot_size_medium
                else -> R.dimen.adfox_carousel_indicator_dot_size_small
            }
            view.layoutParams = getLayoutParams(getResource(size))
            view.isVisible = dotsImageViews.size <= 4 || distance <= 3
            view.alpha = if (page == selectedPage) DOT_ALPHA_SELECTED
                else DOT_ALPHA_NOT_SELECTED
        }
    }

    private fun getLayoutParams(dotSize: Int): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(dotSize, dotSize)
        layoutParams.setMargins(getResource(R.dimen.adfox_carousel_indicator_dot_margin))
        return layoutParams
    }

    private fun getResource(@DimenRes id: Int): Int {
        return indicatorLayout.resources.getDimension(id).toInt()
    }

    companion object {

        private const val DOT_ALPHA_SELECTED = 1f
        private const val DOT_ALPHA_NOT_SELECTED = 0.6f
    }
}
