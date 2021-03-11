/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mediation.nativeads.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdEventListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "AdFoxNativeExample";

    private static final String YANDEX_BLOCK_ID = "adf-279013/966633";
    private static final String ADMOB_BLOCK_ID = "adf-279013/975930";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/975933";
    private static final String MOPUB_BLOCK_ID = "adf-279013/975931";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/975932";

    private NativeAdLoader mNativeAdLoader;
    private NativeBannerView mNativeBannerView;

    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNativeBannerView = (NativeBannerView) findViewById(R.id.native_template);

        mSpinner = findViewById(R.id.mediation_spinner);

        final Button nativeAdLoadButton = (Button) findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(mNativeAdLoadClickListener);
    }

    private void createNativeAdLoader() {
        mNativeAdLoader = new NativeAdLoader(this);
        mNativeAdLoader.setNativeAdLoadListener(mNativeAdLoadListener);
    }

    private String getBlockId() {
        /*
         * Replace demo BLOCK_ID with actual Block ID
         * Following demo block ids may be used for testing:
         * Yandex: YANDEX_BLOCK_ID
         * AdMob mediation: ADMOB_BLOCK_ID
         * Facebook mediation: FACEBOOK_BLOCK_ID
         * MoPub mediation: MOPUB_BLOCK_ID
         * MyTarget mediation: MYTARGET_BLOCK_ID
         */
        final String mediation = mSpinner.getSelectedItem().toString();
        switch (mediation) {
            case "Yandex":
                return YANDEX_BLOCK_ID;
            case "AdMob":
                return ADMOB_BLOCK_ID;
            case "Facebook":
                return FACEBOOK_BLOCK_ID;
            case "MoPub":
                return MOPUB_BLOCK_ID;
            case "MyTarget":
                return MYTARGET_BLOCK_ID;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void bindNativeAd(@NonNull final NativeAd nativeAd) {
        Log.d(SAMPLE_TAG, String.format("Info: %s", nativeAd.getInfo()));

        nativeAd.setNativeAdEventListener(mNativeAdEventListener);
        mNativeBannerView.setAd(nativeAd);
        mNativeBannerView.setVisibility(View.VISIBLE);
    }

    private void refreshNativeAd() {
        createNativeAdLoader();
        mNativeBannerView.setVisibility(View.GONE);

        final String blockId = getBlockId();
        final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                new NativeAdRequestConfiguration.Builder(blockId)
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
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad, error = " + error, Toast.LENGTH_SHORT).show();
        }
    };

    private final NativeAdEventListener mNativeAdEventListener = new NativeAdEventListener() {

        @Override
        public void onLeftApplication() {
            Log.d(SAMPLE_TAG, "onLeftApplication");
        }

        @Override
        public void onReturnedToApplication() {
            Log.d(SAMPLE_TAG, "onReturnedToApplication");
        }
    };
}