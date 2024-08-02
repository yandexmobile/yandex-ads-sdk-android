/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfoxcarousel

import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewPagerAutoscroller(private val viewPager2: ViewPager2) {

    private val itemCount get() = viewPager2.adapter!!.itemCount
    private val currentItem get() = viewPager2.currentItem

    private val scope = MainScope()
    private var job: Job? = null
    private var lastScroll = 0L

    fun startScrolling() {
        stopScrolling()
        updateLastScroll()
        job = scope.launch {
            while (true) {
                delay(CHECK_INTERVAL_MILLIS)
                if (System.currentTimeMillis() - lastScroll > SCROLL_INTERVAL_MILLIS) {
                    scroll()
                }
            }
        }
    }

    fun updateLastScroll() {
        lastScroll = System.currentTimeMillis()
    }

    fun stopScrolling() {
        job?.cancel()
        job = null
    }

    private fun scroll() {
        openNextPage()
        updateLastScroll()
    }

    private fun openNextPage() {
        val nextItem = (currentItem + 1) % itemCount
        viewPager2.setCurrentItem(nextItem, true)
    }

    companion object {

        private const val SCROLL_INTERVAL_MILLIS = 5000L
        private const val CHECK_INTERVAL_MILLIS = 1000L
    }
}
