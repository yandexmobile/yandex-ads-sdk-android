/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2016 YANDEX
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "AdFoxNativeExample";

    private AdRequest mAdRequest;
    private NativeAdLoader mNativeAdLoader;
    private NativeBannerView mNativeBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdRequest = createAdRequest();
        mNativeBannerView = (NativeBannerView) findViewById(R.id.native_template);
        createNativeAdLoader();

        final Button nativeAdLoadButton = (Button) findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(mNativeAdLoadClickListener);
    }

    private AdRequest createAdRequest() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "168627");
        parameters.put("adf_p1", "bvyhx");
        parameters.put("adf_p2", "fksh");
        parameters.put("adf_pfc", "a");
        parameters.put("adf_pfb", "a");
        parameters.put("adf_plp", "a");
        parameters.put("adf_pli", "a");
        parameters.put("adf_pop", "a");
        parameters.put("adf_pt", "b");

        return AdRequest.builder().withParameters(parameters).build();
    }

    private void createNativeAdLoader() {
        /*
        * Replace demo R-M-187883-1 with actual Block ID
        * Please, note, that configured image sizes don't affect demo ads.
        * Following demo Block IDs may be used for testing:
        * image ad: R-M-187883-1
        */
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder("R-M-187883-1", true).build();
        mNativeAdLoader = new NativeAdLoader(this, adLoaderConfiguration);
        mNativeAdLoader.setOnLoadListener(mNativeAdLoadListener);
    }

    private void bindNativeAd(@NonNull final NativeGenericAd nativeAd) {
        Log.d(SAMPLE_TAG, String.format("Info: %s", nativeAd.getInfo()));

        nativeAd.setAdEventListener(mNativeAdEventListener);
        mNativeBannerView.setAd(nativeAd);
        mNativeBannerView.setVisibility(View.VISIBLE);
    }

    private void refreshNativeAd() {
        mNativeBannerView.setVisibility(View.GONE);
        mNativeAdLoader.loadAd(mAdRequest);
    }

    private View.OnClickListener mNativeAdLoadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private NativeAdLoader.OnLoadListener mNativeAdLoadListener = new NativeAdLoader.OnImageAdLoadListener() {

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
            Log.d(SAMPLE_TAG, error.getDescription());
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