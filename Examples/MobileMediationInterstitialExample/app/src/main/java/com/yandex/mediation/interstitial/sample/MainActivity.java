/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mediation.interstitial.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.InterstitialAd;
import com.yandex.mobile.ads.InterstitialEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String YANDEX_BLOCK_ID = "adf-279013/966533";
    private static final String ADMOB_BLOCK_ID = "adf-279013/971987";
    private static final String APPLOVIN_BLOCK_ID = "adf-279013/1052102";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/975925";
    private static final String IRONSOURCE_BLOCK_ID = "adf-279013/1052105";
    private static final String MOPUB_BLOCK_ID = "adf-279013/975923";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/975924";
    private static final String STARTAPP_BLOCK_ID = "adf-279013/1004808";
    private static final String UNITYADS_BLOCK_ID = "adf-279013/1004804";

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
         * Replace demo BLOCK_ID with actual Block ID
         * Following demo block ids may be used for testing:
         * Yandex: YANDEX_BLOCK_ID
         * AdMob mediation: ADMOB_BLOCK_ID
         * AppLovin mediation: APPLOVIN_BLOCK_ID
         * Facebook mediation: FACEBOOK_BLOCK_ID
         * IronSource mediation: IRONSOURCE_BLOCK_ID
         * MoPub mediation: MOPUB_BLOCK_ID
         * MyTarget mediation: MYTARGET_BLOCK_ID
         * StartApp mediation: STARTAPP_BLOCK_ID
         * UnityAds mediation: UNITYADS_BLOCK_ID
         */
        mInterstitialAd.setBlockId(ADMOB_BLOCK_ID);
        mInterstitialAd.setInterstitialEventListener(mInterstitialAdEventListener);
    }

    @Override
    protected void onDestroy() {
        mInterstitialAd.setInterstitialEventListener(null);
        mInterstitialAd.destroy();
        super.onDestroy();
    }

    private View.OnClickListener mInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoadInterstitialAdButton.setEnabled(false);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.start_load_interstitial_button));

            mInterstitialAd.loadAd(AdRequest.builder().build());
        }
    };

    private InterstitialEventListener mInterstitialAdEventListener = new InterstitialEventListener.SimpleInterstitialEventListener() {

        @Override
        public void onInterstitialLoaded() {
            mInterstitialAd.show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onInterstitialFailedToLoad(final AdRequestError error) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad, error = " + error, Toast.LENGTH_SHORT).show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }
    };
}
