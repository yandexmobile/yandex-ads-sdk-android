/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.nativeads.template.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "NativeTemplatesExample";

    private NativeAdLoader mNativeAdLoader;
    private NativeBannerView mNativeBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button nativeAdLoadButton = findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(mNativeAdLoadClickListener);

        mNativeBannerView = findViewById(R.id.native_template);
        createNativeAdLoader();
    }

    private void createNativeAdLoader() {
        /*
        * Replace demo R-M-DEMO-native-i with actual Block ID
        * Please, note, that configured image sizes don't affect demo ads.
        * Following demo Block IDs may be used for testing:
        * app install: R-M-DEMO-native-i
        * content: R-M-DEMO-native-c
        */

        mNativeAdLoader = new NativeAdLoader(this);
        mNativeAdLoader.setNativeAdLoadListener(mNativeAdLoadListener);
    }

    private void refreshNativeAd() {
        mNativeBannerView.setVisibility(View.GONE);

        final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                new NativeAdRequestConfiguration.Builder("R-M-DEMO-native-i")
                        .setShouldLoadImagesAutomatically(true)
                        .build();
        mNativeAdLoader.loadAd(nativeAdRequestConfiguration);
    }

    private final View.OnClickListener mNativeAdLoadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private final NativeAdLoadListener mNativeAdLoadListener = new NativeAdLoadListener() {

        @Override
        public void onAdLoaded(@NonNull final NativeAd nativeAd) {
            bindNativeAd(nativeAd);
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
            Log.d(SAMPLE_TAG, adRequestError.getDescription());
        }

    };

    private void bindNativeAd(@NonNull final NativeAd nativeAd) {
        mNativeBannerView.setAd(nativeAd);
        mNativeBannerView.setVisibility(View.VISIBLE);
    }
}