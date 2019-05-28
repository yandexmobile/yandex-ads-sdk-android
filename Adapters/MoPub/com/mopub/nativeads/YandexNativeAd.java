/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.mopub.nativeads;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.View;

import com.yandex.mobile.ads.nativeads.NativeAdEventListener;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;

class YandexNativeAd extends BaseNativeAd {

    @NonNull
    private final NativeGenericAd mNativeGeneticAd;

    YandexNativeAd(@NonNull final NativeGenericAd nativeGenericAd) {
        mNativeGeneticAd = nativeGenericAd;
        mNativeGeneticAd.setAdEventListener(new YandexNativeAdEventListener());
    }

    @NonNull
    NativeGenericAd getNativeGeneticAd() {
        return mNativeGeneticAd;
    }

    @Override
    public void prepare(@NonNull final View view) {
    }

    @Override
    public void clear(@NonNull final View view) {
    }

    @Override
    public void destroy() {
    }

    private final class YandexNativeAdEventListener extends NativeAdEventListener.SimpleNativeAdEventListener {

        @Override
        public void onAdLeftApplication() {
            notifyAdClicked();
        }

        @Override
        public void onAdOpened() {
            notifyAdClicked();
        }

        @Keep
        public void onAdImpressionTracked() {
            notifyAdImpressed();
        }
    }
}
