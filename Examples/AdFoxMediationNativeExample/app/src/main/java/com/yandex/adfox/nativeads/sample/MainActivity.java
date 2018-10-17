/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
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
        /*
        * Replace demo MediationConfigurator.ADMOB_DEMO_NATIVE with actual AdFox parameters.
        * Following demo parameters may be used for testing:
        * Yandex: MediationConfigurator.ADFOX_DEMO_NATIVE
        * AdMob mediation: MediationConfigurator.ADMOB_DEMO_NATIVE
        * Facebook mediation: MediationConfigurator.FACEBOOK_DEMO_NATIVE
        * MoPub mediation: MediationConfigurator.MOPUB_DEMO_NATIVE
        * MyTarget mediation: MediationConfigurator.MYTARGET_DEMO_NATIVE
        * StartApp mediation: MediationConfigurator.STARTAPP_DEMO_NATIVE
        */
        return AdRequest.builder()
                .withParameters(MediationConfigurator.MOPUB_DEMO_NATIVE)
                .build();
    }

    private void createNativeAdLoader() {
        /*
        * Replace demo R-M-206876-21 with actual Block ID
        * Please, note, that configured image sizes don't affect demo ads.
        * Following demo Block IDs may be used for testing:
        * image ad: R-M-206876-21
        */
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder("R-M-206876-21", true).build();
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