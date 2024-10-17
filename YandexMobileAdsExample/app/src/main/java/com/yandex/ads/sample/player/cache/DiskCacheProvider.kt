/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.player.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache

@OptIn(UnstableApi::class)
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
        val databaseProvider = StandaloneDatabaseProvider(context)

        return SimpleCache(cacheDir, cacheEvictor, databaseProvider)
    }
}
