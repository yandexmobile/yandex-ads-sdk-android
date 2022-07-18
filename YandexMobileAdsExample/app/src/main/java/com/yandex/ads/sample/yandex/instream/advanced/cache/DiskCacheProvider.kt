/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.yandex.instream.advanced.cache

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

object DiskCacheProvider {

    private const val CACHE_DIRECTORY_NAME = "video-cache"
    private const val MIN_DISK_CACHE_SIZE_BYTES = 40 * 1024 * 1024L

    private val diskCachePathProvider = DiskCachePathProvider()

    private var cache: Cache? = null

    fun getCache(context: Context): Cache {
        val appContext = context.applicationContext
        return cache ?: createCache(appContext).also {
            cache = it
        }
    }

    private fun createCache(context: Context): Cache {
        val cacheDir = diskCachePathProvider.getDiskCacheDirectory(context, CACHE_DIRECTORY_NAME)
        val cacheEvictor = LeastRecentlyUsedCacheEvictor(MIN_DISK_CACHE_SIZE_BYTES)
        val databaseProvider = ExoDatabaseProvider(context)

        return SimpleCache(cacheDir, cacheEvictor, databaseProvider)
    }
}
