/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.ironsourceadapter.rewarded.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

public class MainActivity extends AppCompatActivity {

    private static final String APP_KEY = "85460dcd";
    private static final String TAG = "MainActivity";

    private Button mLoadRewardedAdButton;
    private Button mShowRewardedAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadRewardedAdButton = findViewById(R.id.load_rewarded_button);
        mLoadRewardedAdButton.setOnClickListener(mLoadRewardedClickListener);

        mShowRewardedAdButton = findViewById(R.id.show_rewarded_button);
        mShowRewardedAdButton.setOnClickListener(mShowRewardedClickListener);
        mShowRewardedAdButton.setEnabled(false);

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

    private final View.OnClickListener mLoadRewardedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull final View v) {
            mLoadRewardedAdButton.setEnabled(false);
            mLoadRewardedAdButton.setText(getResources().getText(R.string.start_load_rewarded_button));

            IronSource.setRewardedVideoListener(new IronSourceRewardedListener());
            IronSource.loadRewardedVideo();
        }
    };

    private final View.OnClickListener mShowRewardedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull final View v) {
            if (IronSource.isInterstitialReady()) {
                IronSource.showInterstitial();
            }

            mShowRewardedAdButton.setEnabled(false);
        }
    };

    private class IronSourceRewardedListener implements RewardedVideoListener {

        @Override
        public void onRewardedVideoAdOpened() {
            mShowRewardedAdButton.post(() -> {
                mShowRewardedAdButton.setEnabled(true);
                mLoadRewardedAdButton.setEnabled(true);
                mLoadRewardedAdButton.setText(getResources().getText(R.string.load_rewarded_button));
            });
        }

        @Override
        public void onRewardedVideoAdClosed() {
            Log.d(TAG, "onRewardedVideoAdClosed()");
        }

        @Override
        public void onRewardedVideoAvailabilityChanged(boolean b) {
            Log.d(TAG, "onRewardedVideoAvailabilityChanged()");
        }

        @Override
        public void onRewardedVideoAdStarted() {
            Log.d(TAG, "onRewardedVideoAdStarted()");
        }

        @Override
        public void onRewardedVideoAdEnded() {
            Log.d(TAG, "onRewardedVideoAdEnded()");
        }

        @Override
        public void onRewardedVideoAdRewarded(Placement placement) {
            Log.d(TAG, "onRewardedVideoAdRewarded()");
        }

        @Override
        public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
            final String errorMessage = String.format("onRewardedVideoAdShowFailed() code = %s, message = %s",
                    ironSourceError.getErrorCode(), ironSourceError.getErrorMessage());
            Log.d(TAG, errorMessage);
            mLoadRewardedAdButton.post(() -> {
                mLoadRewardedAdButton.setEnabled(true);
                mLoadRewardedAdButton.setText(getResources().getText(R.string.load_rewarded_button));
            });
        }

        @Override
        public void onRewardedVideoAdClicked(Placement placement) {
            Log.d(TAG, "onRewardedVideoAdClicked()");
        }
    }
}
