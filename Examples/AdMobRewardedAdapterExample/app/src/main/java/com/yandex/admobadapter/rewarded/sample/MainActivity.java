/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.admobadapter.rewarded.sample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY";

    private Button mLoadAdButton;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadAdButton = (Button) findViewById(R.id.load_button);
        mLoadAdButton.setOnClickListener(new RewardedAdClickListener());
    }

    @Override
    protected void onDestroy() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.setRewardedVideoAdListener(null);
        }
        super.onDestroy();
    }

    @NonNull
    private RewardedVideoAd createRewardedAd() {
        final RewardedVideoAd rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        final RewardedAdListener rewardedAdListener = new RewardedAdListener();
        rewardedVideoAd.setRewardedVideoAdListener(rewardedAdListener);

        return rewardedVideoAd;
    }

    private class RewardedAdClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mLoadAdButton.setEnabled(false);
            mLoadAdButton.setText(getResources().getText(R.string.start_load_ad_button));

            mRewardedVideoAd = createRewardedAd();

            /*
             Replace AD_UNIT_ID with your unique Ad Unit ID.
             Please, read official documentation how to obtain one: https://apps.admob.co
             */
            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    };

    private class RewardedAdListener implements RewardedVideoAdListener {

        @Override
        public void onRewardedVideoAdLoaded() {
            if (mRewardedVideoAd != null) {
                mRewardedVideoAd.show();
            }

            mLoadAdButton.setEnabled(true);
            mLoadAdButton.setText(getResources().getText(R.string.load_button));
        }

        @Override
        public void onRewarded(final RewardItem rewardItem) {
            final String rewardedCallbackMessage = String.format(Locale.US, "onRewarded(), %d %s",
                    rewardItem.getAmount(), rewardItem.getType());
            Toast.makeText(MainActivity.this, rewardedCallbackMessage, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(final int errorCode) {
            mLoadAdButton.setEnabled(true);
            mLoadAdButton.setText(getResources().getText(R.string.load_button));

            Toast.makeText(MainActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdOpened() {}

        @Override
        public void onRewardedVideoStarted() {}

        @Override
        public void onRewardedVideoAdClosed() {}

        @Override
        public void onRewardedVideoAdLeftApplication() {}

        @Override
        public void onRewardedVideoCompleted() {}
    };
}
