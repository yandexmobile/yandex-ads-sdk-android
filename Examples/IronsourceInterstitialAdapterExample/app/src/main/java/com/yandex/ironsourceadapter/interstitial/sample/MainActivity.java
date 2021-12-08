/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.ironsourceadapter.interstitial.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

public class MainActivity extends AppCompatActivity {

    private static final String APP_KEY = "85460dcd";
    private static final String TAG = "MainActivity";

    private Button mLoadInterstitialAdButton;
    private Button mShowInterstitialAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadInterstitialAdButton = findViewById(R.id.load_interstitial_button);
        mLoadInterstitialAdButton.setOnClickListener(mLoadInterstitialClickListener);

        mShowInterstitialAdButton = findViewById(R.id.show_interstitial_button);
        mShowInterstitialAdButton.setOnClickListener(mShowInterstitialClickListener);
        mShowInterstitialAdButton.setEnabled(false);

        IntegrationHelper.validateIntegration(this);

        IronSource.init(this, APP_KEY, IronSource.AD_UNIT.INTERSTITIAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    protected void onDestroy() {
        IronSource.setInterstitialListener(null);
        super.onDestroy();
    }

    private final View.OnClickListener mLoadInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull final View v) {
            mLoadInterstitialAdButton.setEnabled(false);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.start_load_interstitial_button));

            IronSource.setInterstitialListener(new IronSourceInterstitialListener());
            IronSource.loadInterstitial();
        }
    };

    private final View.OnClickListener mShowInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull final View v) {
            if (IronSource.isInterstitialReady()) {
                IronSource.showInterstitial();
            }

            mShowInterstitialAdButton.setEnabled(false);
        }
    };

    private class IronSourceInterstitialListener implements InterstitialListener {

        @Override
        public void onInterstitialAdReady() {
            mShowInterstitialAdButton.post(() -> {
                mShowInterstitialAdButton.setEnabled(true);
                mLoadInterstitialAdButton.setEnabled(true);
                mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
            });
        }

        @Override
        public void onInterstitialAdLoadFailed(@NonNull final IronSourceError ironSourceError) {
            final String errorMessage = String.format("onInterstitialAdLoadFailed() code = %s, message = %s",
                    ironSourceError.getErrorCode(), ironSourceError.getErrorMessage());
            Log.d(TAG, errorMessage);
            mLoadInterstitialAdButton.post(() -> {
                mLoadInterstitialAdButton.setEnabled(true);
                mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
            });
        }

        @Override
        public void onInterstitialAdOpened() {
            Log.d(TAG, "onInterstitialAdOpened()");
        }

        @Override
        public void onInterstitialAdClosed() {
            Log.d(TAG, "onInterstitialAdClosed()");
        }

        @Override
        public void onInterstitialAdShowSucceeded() {
            Log.d(TAG, "onInterstitialAdShowSucceeded()");
        }

        @Override
        public void onInterstitialAdShowFailed(@NonNull final IronSourceError ironSourceError) {
            final String errorMessage = String.format("onInterstitialAdShowFailed() code = %s, message = %s",
                    ironSourceError.getErrorCode(), ironSourceError.getErrorMessage());
            Log.d(TAG, errorMessage);
            mLoadInterstitialAdButton.post(() -> {
                mLoadInterstitialAdButton.setEnabled(true);
                mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
            });
        }

        @Override
        public void onInterstitialAdClicked() {
            Log.d(TAG, "onInterstitialAdClicked()");
        }
    }
}
