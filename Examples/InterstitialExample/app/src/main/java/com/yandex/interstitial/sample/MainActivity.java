/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.interstitial.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;


public class MainActivity extends AppCompatActivity {

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
        * Replace demo R-M-DEMO-320x480 with actual Block ID
        * Following demo Block IDs may be used for testing:
        * R-M-DEMO-320x480
        * R-M-DEMO-480x320
        * R-M-DEMO-400x240-context
        * R-M-DEMO-240x400-context
        * R-M-DEMO-video-interstitial
        */
        mInterstitialAd.setBlockId("R-M-DEMO-320x480");

        mAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.setInterstitialAdEventListener(mInterstitialAdEventListener);
    }

    @Override
    protected void onDestroy() {
        mInterstitialAd.destroy();
        super.onDestroy();
    }

    private View.OnClickListener mInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoadInterstitialAdButton.setEnabled(false);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.start_load_interstitial_button));

            mInterstitialAd.loadAd(mAdRequest);
        }
    };

    private InterstitialAdEventListener mInterstitialAdEventListener = new InterstitialAdEventListener() {

        @Override
        public void onAdLoaded() {
            Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
            mInterstitialAd.show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
            Toast.makeText(MainActivity.this, adRequestError.getDescription(), Toast.LENGTH_SHORT).show();
            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onAdShown() {
            Toast.makeText(MainActivity.this, "onAdShown", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdDismissed() {
            Toast.makeText(MainActivity.this, "onAdDismissed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLeftApplication() {
            Toast.makeText(MainActivity.this, "onLeftApplication", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReturnedToApplication() {
            Toast.makeText(MainActivity.this, "onReturnedToApplication", Toast.LENGTH_SHORT).show();
        }
    };
}
