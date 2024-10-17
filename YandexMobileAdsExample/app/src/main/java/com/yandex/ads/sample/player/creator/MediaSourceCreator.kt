/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.ads.sample.player.creator

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import com.yandex.ads.sample.player.cache.DiskCacheProvider

@OptIn(UnstableApi::class)
class MediaSourceCreator(
    private val context: Context,
) {

    fun createMediaSource(streamUrl: String): MediaSource {
        val cache = DiskCacheProvider.getCache(context)
        val adMediaItem = MediaItem.fromUri(streamUrl)
        val userAgent = Util.getUserAgent(context, context.packageName)

        val defaultDataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(userAgent)
        val adPlayerCacheFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)

        return DefaultMediaSourceFactory(adPlayerCacheFactory).createMediaSource(adMediaItem)
    }
}
