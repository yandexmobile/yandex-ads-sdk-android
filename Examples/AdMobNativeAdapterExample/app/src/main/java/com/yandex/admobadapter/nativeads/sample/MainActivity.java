/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.admobadapter.nativeads.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.admob.mobileads.YandexNative;
import com.admob.mobileads.nativeads.YandexNativeAdAsset;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY";

    private AdMobNativeAdBinder mAdMobNativeAdBinder;
    private UnifiedNativeAdView mNativeAdView;
    private AdLoader mNativeAdLoader;
    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Replace demo AD_UNIT_ID with actual Ad Unit Id
         */
        mNativeAdLoader = new AdLoader.Builder(this, AD_UNIT_ID)
                .withAdListener(new NativeAdListener())
                .forUnifiedNativeAd(new UnifiedNativeAdLoadedListener())
                .build();

        mExtras = createCustomEventExtras();
        mNativeAdView = findViewById(R.id.admob_native_ad_view);

        mAdMobNativeAdBinder = new AdMobNativeAdBinder();

        final Button nativeAdLoadButton = findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(new NativeAdLoadClickListener());
    }

    private Bundle createCustomEventExtras() {
        final Bundle extras = new Bundle();
        extras.putInt(YandexNativeAdAsset.AGE, R.id.age);
        extras.putInt(YandexNativeAdAsset.DOMAIN, R.id.domain);
        extras.putInt(YandexNativeAdAsset.FAVICON, R.id.favicon);
        extras.putInt(YandexNativeAdAsset.FEEDBACK, R.id.feedback);
        extras.putInt(YandexNativeAdAsset.RATING, R.id.domain);
        extras.putInt(YandexNativeAdAsset.REVIEW_COUNT, R.id.review_count);
        extras.putInt(YandexNativeAdAsset.SPONSORED, R.id.sponsored);
        extras.putInt(YandexNativeAdAsset.WARNING, R.id.warning);

        return extras;
    }

    private void refreshNativeAd() {
        mNativeAdView.setVisibility(View.GONE);
        mNativeAdLoader.loadAd(new AdRequest.Builder()
                .addCustomEventExtrasBundle(YandexNative.class, mExtras)
                .build());
    }

    private void bindNativeAd(final UnifiedNativeAd unifiedNativeAd) {
        mAdMobNativeAdBinder.clearNativeAdView(mNativeAdView);
        mAdMobNativeAdBinder.bindNativeAd(unifiedNativeAd, mNativeAdView);
        mNativeAdView.setVisibility(View.VISIBLE);
    }

    private class NativeAdLoadClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private class UnifiedNativeAdLoadedListener implements UnifiedNativeAd.OnUnifiedNativeAdLoadedListener {
        @Override
        public void onUnifiedNativeAdLoaded(final UnifiedNativeAd unifiedNativeAd) {
            bindNativeAd(unifiedNativeAd);
        }
    }

    private class NativeAdListener extends AdListener {
        @Override
        public void onAdFailedToLoad(final LoadAdError loadAdError) {
            final int errorCode = loadAdError.getCode();
            final String errorMessage = "onAdFailedToLoad, error code = " + errorCode;
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
