/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.admobadapter.interstitial.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY";

    private AdRequest mAdRequest;
    private InterstitialAd mInterstitialAd;

    private Button mLoadInterstitialAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadInterstitialAdButton = (Button) findViewById(R.id.load_interstitial_button);
        mLoadInterstitialAdButton.setOnClickListener(mInterstitialClickListener);

        initInterstitialAd();
    }

    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);

        /*
          Replace AD_UNIT_ID with your unique Ad Unit ID.
          Please, read official documentation how to obtain one: https://apps.admob.com
        */
        mInterstitialAd.setAdUnitId(AD_UNIT_ID);

        mAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(mInterstitialAdListener);
    }

    private final View.OnClickListener mInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoadInterstitialAdButton.setEnabled(false);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.start_load_interstitial_button));

            mInterstitialAd.loadAd(mAdRequest);
        }
    };

    private final AdListener mInterstitialAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            mInterstitialAd.show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onAdFailedToLoad(final LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }
    };
}
