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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.MediaView;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdMedia;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAdView;
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "NativeAdSample";

    private NativeAdView mNativeAdView;

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
        * image: R-M-DEMO-native-video
        */
        mNativeAdLoader = new NativeAdLoader(this);
        mNativeAdLoader.setNativeAdLoadListener(mNativeAdLoadListener);
    }

    private View.OnClickListener mNativeAdLoadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private void refreshNativeAd() {
        mNativeAdView.setVisibility(View.GONE);

        final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                new NativeAdRequestConfiguration.Builder("R-M-DEMO-native-i")
                        .setShouldLoadImagesAutomatically(true)
                        .build();
        mNativeAdLoader.loadAd(nativeAdRequestConfiguration);
    }

    private NativeAdLoadListener mNativeAdLoadListener = new NativeAdLoadListener() {

        @Override
        public void onAdLoaded(@NonNull final NativeAd nativeAd) {
            bindNativeAd(nativeAd);
        }

        @Override
        public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
            Log.d(SAMPLE_TAG, adRequestError.getDescription());
            Toast.makeText(MainActivity.this, adRequestError.getDescription(), Toast.LENGTH_SHORT).show();
        }


        private void bindNativeAd(@NonNull final NativeAd nativeAd) {
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

            configureMediaView(nativeAd);

            try {
                nativeAd.bindNativeAd(nativeAdViewBinder);
                mNativeAdView.setVisibility(View.VISIBLE);
            } catch (NativeAdException exception) {
                Log.d(SAMPLE_TAG, exception.getMessage());
            }
        }

        private void configureMediaView(@NonNull final NativeAd nativeAd) {
            final NativeAdMedia nativeAdMedia = nativeAd.getAdAssets().getMedia();
            if (nativeAdMedia != null) {
                //you can use the aspect ratio if you need it to determine the size of media view.
                final float aspectRatio = nativeAdMedia.getAspectRatio();
            }
        }
    };
}