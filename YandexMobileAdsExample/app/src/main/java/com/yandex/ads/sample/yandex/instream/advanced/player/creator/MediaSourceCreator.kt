/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.ads.sample.yandex.instream.advanced.player.creator

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.Util
import com.yandex.ads.sample.yandex.instream.advanced.cache.DiskCacheProvider

class MediaSourceCreator(
    private val context: Context
) {

    fun createMediaSource(streamUrl: String): MediaSource {
        val cache = DiskCacheProvider.getCache(context)
        val adMediaItem = MediaItem.fromUri(streamUrl)
        val userAgent = Util.getUserAgent(context, context.packageName)
        val defaultDataSourceFactory = DefaultDataSourceFactory(context, userAgent)
        val adPlayerCacheFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)

        return DefaultMediaSourceFactory(adPlayerCacheFactory).createMediaSource(adMediaItem)
    }
}
