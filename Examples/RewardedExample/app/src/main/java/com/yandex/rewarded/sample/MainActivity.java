/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.rewarded.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String BLOCK_ID = "R-M-DEMO-rewarded-client-side-rtb";

    private Button mLoadRewardedAdButton;

    private RewardedAd mRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadRewardedAdButton = findViewById(R.id.load_button);
        mLoadRewardedAdButton.setOnClickListener(new LoadButtonClickListener());
    }

    @NonNull
    private RewardedAd createRewardedAd() {
        final RewardedAd rewardedAd = new RewardedAd(this);

        /*
         * Replace demo BLOCK_ID with actual Block ID
         */
        rewardedAd.setBlockId(BLOCK_ID);
        rewardedAd.setRewardedAdEventListener(new RewardedAdListener());

        return rewardedAd;
    }

    @Override
    protected void onDestroy() {
        if (mRewardedAd != null) {
            mRewardedAd.setRewardedAdEventListener(null);
            mRewardedAd.destroy();
        }

        super.onDestroy();
    }

    private class LoadButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            mLoadRewardedAdButton.setEnabled(false);
            mLoadRewardedAdButton.setText(getResources().getText(R.string.start_load_button));

            mRewardedAd = createRewardedAd();
            mRewardedAd.loadAd(new AdRequest.Builder().build());
        }
    }

    private class RewardedAdListener implements RewardedAdEventListener {

        @Override
        public void onAdLoaded() {
            Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
            if (mRewardedAd != null) {
                mRewardedAd.show();
            }

            mLoadRewardedAdButton.setEnabled(true);
            mLoadRewardedAdButton.setText(getResources().getText(R.string.load_button));
        }

        @Override
        public void onRewarded(@NonNull final Reward reward) {
            final String message = String.format("onRewarded, amount = %s, type = %s", reward.getAmount(), reward.getType());
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
            final String message = String.format("onAdFailedToLoad, error = %s", adRequestError.getDescription());
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            mLoadRewardedAdButton.setEnabled(true);
            mLoadRewardedAdButton.setText(getResources().getText(R.string.load_button));
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
    }
}
