/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.adfox.nativeads.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdEventListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeContentAd;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;
import com.yandex.mobile.ads.nativeads.NativeImageAd;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "AdFoxNativeExample";

    private static final String ADFOX_BLOCK_ID = "adf-279013/966633";
    private static final String ADMOB_BLOCK_ID = "adf-279013/975930";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/975933";
    private static final String MOPUB_BLOCK_ID = "adf-279013/975931";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/975932";

    private NativeAdLoader mNativeAdLoader;
    private NativeBannerView mNativeBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNativeBannerView = (NativeBannerView) findViewById(R.id.native_template);
        createNativeAdLoader();

        final Button nativeAdLoadButton = (Button) findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(mNativeAdLoadClickListener);
    }

    private void createNativeAdLoader() {
        /*
         * Replace demo BLOCK_ID with actual Block ID
         * Following demo block ids may be used for testing:
         * Yandex: ADFOX_BLOCK_ID
         * AdMob mediation: ADMOB_BLOCK_ID
         * Facebook mediation: FACEBOOK_BLOCK_ID
         * MyTarget mediation: MOPUB_BLOCK_ID
         * MyTarget mediation: MYTARGET_BLOCK_ID
         */
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder(ADMOB_BLOCK_ID, true).build();
        mNativeAdLoader = new NativeAdLoader(this, adLoaderConfiguration);
        mNativeAdLoader.setNativeAdLoadListener(mNativeAdLoadListener);
    }

    private void bindNativeAd(@NonNull final NativeGenericAd nativeAd) {
        Log.d(SAMPLE_TAG, String.format("Info: %s", nativeAd.getInfo()));

        nativeAd.setAdEventListener(mNativeAdEventListener);
        mNativeBannerView.setAd(nativeAd);
        mNativeBannerView.setVisibility(View.VISIBLE);
    }

    private void refreshNativeAd() {
        mNativeBannerView.setVisibility(View.GONE);
        mNativeAdLoader.loadAd(AdRequest.builder().build());
    }

    private View.OnClickListener mNativeAdLoadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private NativeAdLoader.OnImageAdLoadListener mNativeAdLoadListener = new NativeAdLoader.OnImageAdLoadListener() {

        @Override
        public void onImageAdLoaded(@NonNull final NativeImageAd nativeImageAd) {
            bindNativeAd(nativeImageAd);
        }

        @Override
        public void onAppInstallAdLoaded(@NonNull final NativeAppInstallAd nativeAppInstallAd) {
            bindNativeAd(nativeAppInstallAd);
        }

        @Override
        public void onContentAdLoaded(@NonNull NativeContentAd nativeContentAd) {
            bindNativeAd(nativeContentAd);
        }

        @Override
        public void onAdFailedToLoad(@NonNull AdRequestError error) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad, error = " + error, Toast.LENGTH_SHORT).show();
        }
    };

    private NativeAdEventListener mNativeAdEventListener = new NativeAdEventListener() {

        @Override
        public void onAdClosed() {
            Log.d(SAMPLE_TAG, "onAdClosed");
        }

        @Override
        public void onAdLeftApplication() {
            Log.d(SAMPLE_TAG, "onAdLeftApplication");
        }

        @Override
        public void onAdOpened() {
            Log.d(SAMPLE_TAG, "onAdOpened");
        }
    };
}