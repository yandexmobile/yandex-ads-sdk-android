/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.slider.sample.ads;

import android.util.Log;

import androidx.annotation.NonNull;

import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.SliderAd;
import com.yandex.mobile.ads.nativeads.SliderAdView;
import com.yandex.nativeads.slider.sample.pager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NativeAdsBinder {

    private static final String NATIVE_ADS_BINDER_TAG = "NativeAdsBinder";

    public void bindAd(@NonNull final SliderAd sliderAd,
                       @NonNull final SliderAdView sliderAdView,
                       @NonNull final ViewPagerAdapter viewPagerAdapter) {
        bindSliderAd(sliderAd, sliderAdView);
        updatePagerNativeAds(sliderAd, viewPagerAdapter);
    }

    private void bindSliderAd(@NonNull final SliderAd sliderAd,
                              @NonNull final SliderAdView sliderAdView) {
        try {
            sliderAd.bindSliderAd(sliderAdView);
        } catch (final NativeAdException exception) {
            Log.e(NATIVE_ADS_BINDER_TAG, exception.getMessage());
        }
    }

    private void updatePagerNativeAds(@NonNull final SliderAd sliderAd,
                                      @NonNull final ViewPagerAdapter viewPagerAdapter) {
        final List<NativeAd> nativeAds = new ArrayList<>(sliderAd.getNativeAds());
        viewPagerAdapter.setData(nativeAds);
    }
}