/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.adunit.sample.ads;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdUnit;
import com.yandex.mobile.ads.nativeads.NativeAdUnitView;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;
import com.yandex.nativeads.adunit.sample.pager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NativeAdsBinder {

    private static final String NATIVE_ADS_BINDER_TAG = "NativeAdsBinder";

    public void bindAd(@NonNull final NativeAdUnit nativeAdUnit,
                       @NonNull final NativeAdUnitView nativeAdUnitView,
                       @NonNull final ViewPagerAdapter viewPagerAdapter) {
        bindNativeAdUnit(nativeAdUnit, nativeAdUnitView);
        updatePagerNativeGenericAds(nativeAdUnit, viewPagerAdapter);
    }

    private void bindNativeAdUnit(@NonNull final NativeAdUnit nativeAdUnit,
                                  @NonNull final NativeAdUnitView nativeAdUnitView) {
        try {
            nativeAdUnit.bindAdUnit(nativeAdUnitView);
        } catch (final NativeAdException exception) {
            Log.e(NATIVE_ADS_BINDER_TAG, exception.getMessage());
        }
    }

    private void updatePagerNativeGenericAds(@NonNull final NativeAdUnit nativeAdUnit,
                                             @NonNull final ViewPagerAdapter viewPagerAdapter) {
        final List<NativeGenericAd> nativeAds = new ArrayList<>(nativeAdUnit.getNativeAds());
        viewPagerAdapter.setData(nativeAds);
    }
}