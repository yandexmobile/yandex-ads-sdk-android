/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mopubadapter.interstitial.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private Button mLoadInterstitialAdButton;
    private MoPubInitializer mMoPubInitializer;
    private MoPubInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadInterstitialAdButton = findViewById(R.id.load_interstitial_button);
        mLoadInterstitialAdButton.setOnClickListener(mInterstitialClickListener);

        mMoPubInitializer = new MoPubInitializer();
        initInterstitialAd();
    }

    private void initInterstitialAd() {
        /*
         * Replace AD_UNIT_ID with your unique Ad Unit ID.
         * Please, read official documentation how to obtain one: {@link https://app.mopub.com}
         */
        mInterstitial = new MoPubInterstitial(this, AD_UNIT_ID);
        mInterstitial.setInterstitialAdListener(mInterstitialAdListener);
    }

    @Override
    protected void onDestroy() {
        if (mInterstitial != null) {
            mInterstitial.destroy();
        }
        super.onDestroy();
    }

    private View.OnClickListener mInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoadInterstitialAdButton.setEnabled(false);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.start_load_interstitial_button));

            mMoPubInitializer.initializeSdk(MainActivity.this, AD_UNIT_ID, () -> {
                mInterstitial.load();
            });
        }
    };

    private MoPubInterstitial.InterstitialAdListener mInterstitialAdListener = new MoPubInterstitial.InterstitialAdListener() {

        @Override
        public void onInterstitialLoaded(final MoPubInterstitial moPubInterstitial) {
            mInterstitial.show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onInterstitialFailed(final MoPubInterstitial moPubInterstitial, final MoPubErrorCode moPubErrorCode) {
            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onInterstitialShown(final MoPubInterstitial interstitial) {
            // do nothing
        }

        @Override
        public void onInterstitialClicked(final MoPubInterstitial interstitial) {
            // do nothing
        }

        @Override
        public void onInterstitialDismissed(final MoPubInterstitial interstitial) {
            // do nothing
        }
    };
}
