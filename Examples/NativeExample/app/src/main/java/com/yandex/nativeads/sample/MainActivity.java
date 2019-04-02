/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.MediaView;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeContentAd;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;
import com.yandex.mobile.ads.nativeads.NativeImageAd;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "NativeAdSample";

    private View mNativeAdView;

    private NativeAdLoader mNativeAdLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button nativeAdLoadButton = (Button) findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(mNativeAdLoadClickListener);

        mNativeAdView = findViewById(R.id.native_ad_container);

        createNativeAd();
    }

    private void createNativeAd() {
        /*
        * Replace demo R-M-DEMO-native-i with actual Block ID
        * Please, note, that configured image sizes don't affect demo ads.
        * Following demo Block IDs may be used for testing:
        * app install: R-M-DEMO-native-i
        * content: R-M-DEMO-native-c
        */
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder("R-M-DEMO-native-i", true)
                        .setImageSizes(NativeAdLoaderConfiguration.NATIVE_IMAGE_SIZE_MEDIUM).build();
        mNativeAdLoader = new NativeAdLoader(this, adLoaderConfiguration);
        mNativeAdLoader.setOnLoadListener(mNativeAdLoadListener);
    }

    private View.OnClickListener mNativeAdLoadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private void refreshNativeAd() {
        mNativeAdView.setVisibility(View.GONE);
        mNativeAdLoader.loadAd(AdRequest.builder().build());
    }

    private NativeAdLoader.OnLoadListener mNativeAdLoadListener = new NativeAdLoader.OnImageAdLoadListener() {

        @Override
        public void onAppInstallAdLoaded(@NonNull final NativeAppInstallAd nativeAppInstallAd) {
            bindNativeAd(nativeAppInstallAd);
        }

        @Override
        public void onContentAdLoaded(@NonNull NativeContentAd nativeContentAd) {
            bindNativeAd(nativeContentAd);
        }

        @Override
        public void onImageAdLoaded(@NonNull final NativeImageAd nativeImageAd) {
            bindNativeAd(nativeImageAd);
        }

        @Override
        public void onAdFailedToLoad(@NonNull AdRequestError error) {
            Log.d(SAMPLE_TAG, error.getDescription());
            Toast.makeText(MainActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
        }

        private void bindNativeAd(@NonNull final NativeGenericAd nativeAd) {
            final NativeAdViewBinder nativeAdViewBinder = new NativeAdViewBinder.Builder(mNativeAdView)
                    .setAgeView((TextView) findViewById(R.id.age))
                    .setBodyView((TextView) findViewById(R.id.body))
                    .setCallToActionView((Button) findViewById(R.id.call_to_action))
                    .setDomainView((TextView) findViewById(R.id.domain))
                    .setFaviconView((ImageView) findViewById(R.id.favicon))
                    .setFeedbackView((Button) findViewById(R.id.feedback))
                    .setIconView((ImageView) findViewById(R.id.icon))
                    .setMediaView((MediaView) findViewById(R.id.media))
                    .setPriceView((TextView) findViewById(R.id.price))
                    .setRatingView((MyRatingView) findViewById(R.id.rating))
                    .setReviewCountView((TextView) findViewById(R.id.review_count))
                    .setSponsoredView((TextView) findViewById(R.id.sponsored))
                    .setTitleView((TextView) findViewById(R.id.title))
                    .setWarningView((TextView) findViewById(R.id.warning))
                    .build();

            try {
                nativeAd.bindNativeAd(nativeAdViewBinder);
                mNativeAdView.setVisibility(View.VISIBLE);
            } catch (NativeAdException exception) {
                Log.d(SAMPLE_TAG, exception.getMessage());
            }
        }
    };
}