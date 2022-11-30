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
import android.os.Environment
import java.io.File

class DiskCachePathProvider {

    fun getDiskCacheDirectory(context: Context, cacheDirName: String): File {
        val externalCacheDir = getExternalCacheDir(context)
        val cacheDir = externalCacheDir ?: context.cacheDir

        return File(cacheDir.path + File.separator + cacheDirName)
    }

    private fun getExternalCacheDir(context: Context): File? {
        var externalCacheDir: File? = null
        try {
            if (isExternalStorageStateAccessible()) {
                val cacheDir = context.externalCacheDir
                if (cacheDir != null && cacheDir.canWrite()) {
                    externalCacheDir = cacheDir
                }
            }
        } catch (ignore: Exception) {
        }

        return externalCacheDir
    }

    private fun isExternalStorageStateAccessible(): Boolean {
        val storageState = Environment.getExternalStorageState()
        val mounted = Environment.MEDIA_MOUNTED == storageState
        val mountedReadOnly = Environment.MEDIA_MOUNTED_READ_ONLY == storageState
        val storageRemovable = Environment.isExternalStorageRemovable()

        return mounted || storageRemovable.not() && mountedReadOnly.not()
    }
}
