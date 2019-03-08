/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.adfox.mediation.rewarded.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String ADFOX_BLOCK_ID = "adf-279013/966487";
    private static final String ADMOB_BLOCK_ID = "adf-279013/966325";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/966329";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/966327";

    private Button mLoadRewardedAdButton;

    private RewardedAd mRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadRewardedAdButton = (Button) findViewById(R.id.load_button);
        mLoadRewardedAdButton.setOnClickListener(new LoadButtonClickListener());
    }

    @NonNull
    private RewardedAd createRewardedAd() {
        final RewardedAd rewardedAd = new RewardedAd(this);

        /*
        * Replace demo BLOCK_ID with actual Block ID
        * Following demo block ids may be used for testing:
        * Yandex: ADFOX_BLOCK_ID
        * AdMob mediation: ADMOB_BLOCK_ID
        * Facebook mediation: FACEBOOK_BLOCK_ID
        * MyTarget mediation: MYTARGET_BLOCK_ID
        */
        rewardedAd.setBlockId(ADFOX_BLOCK_ID);
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
            mRewardedAd.loadAd(AdRequest.builder().build());
        }
    }

    private class RewardedAdListener extends RewardedAdEventListener.SimpleRewardedAdEventListener {

        @Override
        public void onAdFailedToLoad(final AdRequestError adRequestError) {
            final String message = String.format("onAdFailedToLoad, error = %s", adRequestError.getDescription());
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            mLoadRewardedAdButton.setEnabled(true);
            mLoadRewardedAdButton.setText(getResources().getText(R.string.load_button));
        }

        @Override
        public void onAdLoaded() {
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
    }
}
