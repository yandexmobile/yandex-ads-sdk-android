/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2016 YANDEX
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

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdEventListener;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAdView;
import com.yandex.mobile.ads.nativeads.NativeContentAd;
import com.yandex.mobile.ads.nativeads.NativeContentAdView;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "NativeAdSample";

    private NativeContentAdView mContentAdView;
    private NativeAppInstallAdView mAppInstallAdView;

    private NativeAdLoader mNativeAdLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button nativeAdLoadButton = (Button) findViewById(R.id.native_load_button);
        nativeAdLoadButton.setOnClickListener(mNativeAdLoadClickListener);

        mContentAdView = (NativeContentAdView) findViewById(R.id.native_content_ad_container);
        mAppInstallAdView = (NativeAppInstallAdView) findViewById(R.id.native_appinstall_ad_container);

        createNativeAd();
    }

    private void createNativeAd() {
        /*
        * Replace demo R-M-DEMO-native-i with actual Block ID
        * Following demo Block IDs may be used for testing:
        * app install: R-M-DEMO-native-i
        * content: R-M-DEMO-native-c
        */
        mNativeAdLoader = new NativeAdLoader(getApplicationContext(), "R-M-DEMO-native-i");
        mNativeAdLoader.setOnLoadListener(mNativeAdLoadListener);
    }

    private View.OnClickListener mNativeAdLoadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshNativeAd();
        }
    };

    private void refreshNativeAd() {
        mContentAdView.setVisibility(View.GONE);
        mAppInstallAdView.setVisibility(View.GONE);
        mNativeAdLoader.loadAd(AdRequest.builder().build());
    }

    private NativeAdLoader.OnLoadListener mNativeAdLoadListener = new NativeAdLoader.OnLoadListener() {

        @Override
        public void onAppInstallAdLoaded(@NonNull final NativeAppInstallAd nativeAppInstallAd) {
            mAppInstallAdView.setAgeView((TextView) findViewById(R.id.appinstall_age));
            mAppInstallAdView.setBodyView((TextView) findViewById(R.id.appinstall_body));
            mAppInstallAdView.setCallToActionView((Button) findViewById(R.id.appinstall_call_to_action));
            mAppInstallAdView.setIconView((ImageView) findViewById(R.id.appinstall_icon));
            mAppInstallAdView.setPriceView((TextView) findViewById(R.id.appinstall_price));
            mAppInstallAdView.setRatingView((MyRatingView) findViewById(R.id.appinstall_rating));
            mAppInstallAdView.setSponsoredView((TextView) findViewById(R.id.appinstall_sponsored));
            mAppInstallAdView.setTitleView((TextView) findViewById(R.id.appinstall_title));
            mAppInstallAdView.setWarningView((TextView) findViewById(R.id.appinstall_warning));

            bindAppInstallNativeAd(nativeAppInstallAd);
        }

        private void bindAppInstallNativeAd(final NativeAppInstallAd nativeAppInstallAd) {
            try {
                nativeAppInstallAd.setAdEventListener(mNativeAdEventListener);
                nativeAppInstallAd.bindAppInstallAd(mAppInstallAdView);
                mAppInstallAdView.setVisibility(View.VISIBLE);
            } catch (NativeAdException exception) {
                Log.d(SAMPLE_TAG, exception.getMessage());
            }
        }

        @Override
        public void onContentAdLoaded(@NonNull NativeContentAd nativeContentAd) {
            mContentAdView.setAgeView((TextView) findViewById(R.id.content_age));
            mContentAdView.setBodyView((TextView) findViewById(R.id.content_body));
            mContentAdView.setDomainView((TextView) findViewById(R.id.content_domain));
            mContentAdView.setIconView((ImageView) findViewById(R.id.content_favicon));
            mContentAdView.setImageView((ImageView) findViewById(R.id.content_image));
            mContentAdView.setSponsoredView((TextView) findViewById(R.id.content_sponsored));
            mContentAdView.setTitleView((TextView) findViewById(R.id.content_title));
            mContentAdView.setWarningView((TextView) findViewById(R.id.content_warning));

            bindContentNativeAd(nativeContentAd);
        }

        private void bindContentNativeAd(final NativeContentAd nativeContentAd) {
            try {
                nativeContentAd.setAdEventListener(mNativeAdEventListener);
                nativeContentAd.bindContentAd(mContentAdView);
                mContentAdView.setVisibility(View.VISIBLE);
            } catch (NativeAdException exception) {
                Log.d(SAMPLE_TAG, exception.getMessage());
            }
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