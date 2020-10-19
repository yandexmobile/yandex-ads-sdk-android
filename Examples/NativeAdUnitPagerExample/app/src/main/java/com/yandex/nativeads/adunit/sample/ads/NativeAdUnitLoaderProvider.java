/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2020 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.adunit.sample.ads;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAdUnitLoader;

public class NativeAdUnitLoaderProvider {

    private static final String BLOCK_ID = "R-M-DEMO-native-ad-unit";

    @NonNull
    public NativeAdUnitLoader createNativeAdUnitLoader(@NonNull final Context context) {
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder(BLOCK_ID, true).build();

        return new NativeAdUnitLoader(context, adLoaderConfiguration);
    }
}
