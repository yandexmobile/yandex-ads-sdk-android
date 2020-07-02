/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mediation.rewarded.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String YANDEX_BLOCK_ID = "adf-279013/966487";
    private static final String APPLOVIN_BLOCK_ID = "adf-279013/1052103";
    private static final String ADMOB_BLOCK_ID = "adf-279013/966325";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/966329";
    private static final String IRONSOURCE_BLOCK_ID = "adf-279013/1052106";
    private static final String MOPUB_BLOCK_ID = "adf-279013/966326";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/966327";
    private static final String STARTAPP_BLOCK_ID = "adf-279013/1006616";
    private static final String UNITYADS_BLOCK_ID = "adf-279013/1004805";

    private Button mLoadRewardedAdButton;

    private RewardedAd mRewardedAd;

    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = findViewById(R.id.mediation_spinner);
        mLoadRewardedAdButton = (Button) findViewById(R.id.load_button);
        mLoadRewardedAdButton.setOnClickListener(new LoadButtonClickListener());
    }

    @Override
    protected void onDestroy() {
        destroyRewardedAd();

        super.onDestroy();
    }

    private void loadRewardedAd() {
        destroyRewardedAd();
        createRewardedAd();
        mRewardedAd.loadAd(AdRequest.builder().build());
    }

    private void destroyRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd.setRewardedAdEventListener(null);
            mRewardedAd.destroy();
            mRewardedAd = null;
        }
    }

    private void createRewardedAd() {
        final String blockId = getBlockId();
        mRewardedAd = new RewardedAd(this);
        mRewardedAd.setBlockId(blockId);
        mRewardedAd.setRewardedAdEventListener(new RewardedAdListener());
    }

    private String getBlockId() {
        /*
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
        final String mediation = mSpinner.getSelectedItem().toString();
        switch (mediation) {
            case "Yandex":
                return YANDEX_BLOCK_ID;
            case "AdMob":
                return ADMOB_BLOCK_ID;
            case "AppLovin":
                return APPLOVIN_BLOCK_ID;
            case "Facebook":
                return FACEBOOK_BLOCK_ID;
            case "IronSource":
                return IRONSOURCE_BLOCK_ID;
            case "MoPub":
                return MOPUB_BLOCK_ID;
            case "MyTarget":
                return MYTARGET_BLOCK_ID;
            case "StartApp":
                return STARTAPP_BLOCK_ID;
            case "UnityAds":
                return UNITYADS_BLOCK_ID;
            default:
                throw new IllegalArgumentException();
        }
    }

    private class LoadButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            mLoadRewardedAdButton.setEnabled(false);
            mLoadRewardedAdButton.setText(getResources().getText(R.string.start_load_button));

            loadRewardedAd();
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
            mRewardedAd.show();

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
