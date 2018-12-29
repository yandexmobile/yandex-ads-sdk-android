/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mopubadapter.rewarded.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private Button mLoadAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadAdButton = (Button) findViewById(R.id.load_ad_button);
        mLoadAdButton.setOnClickListener(new MoPubRewardedVideosClickListener());

        initializeRewardedVideo();
    }

    @Override
    protected void onDestroy() {
        MoPubRewardedVideos.setRewardedVideoListener(null);
        super.onDestroy();
    }

    private void initializeRewardedVideo() {
        final SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(AD_UNIT_ID).build();
        MoPub.initializeSdk(this, sdkConfiguration, null);

        MoPubRewardedVideos.setRewardedVideoListener(new MoPubRewardedAdEventListener());
    }

    private class MoPubRewardedAdEventListener implements MoPubRewardedVideoListener {
        @Override
        public void onRewardedVideoLoadSuccess(@NonNull final String adUnitId) {
            MoPubRewardedVideos.showRewardedVideo(adUnitId);

            mLoadAdButton.setEnabled(true);
            mLoadAdButton.setText(getResources().getText(R.string.load_ad_button));
        }

        @Override
        public void onRewardedVideoCompleted(@NonNull final Set<String> adUnitIds,
                                             @NonNull final MoPubReward reward) {
            final String message = String.format(Locale.US,
                    "onRewardedVideoCompleted(), reward = %s %s", reward.getAmount(), reward.getLabel());
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoLoadFailure(@NonNull final String adUnitId, @NonNull final MoPubErrorCode errorCode) {
            onRewardedVideoFailed("onRewardedVideoLoadFailure");
        }

        @Override
        public void onRewardedVideoPlaybackError(@NonNull final String adUnitId, @NonNull final MoPubErrorCode errorCode) {
            onRewardedVideoFailed("onRewardedVideoPlaybackError");
        }

        @Override
        public void onRewardedVideoStarted(@NonNull final String adUnitId) {}

        @Override
        public void onRewardedVideoClicked(@NonNull final String adUnitId) {}

        @Override
        public void onRewardedVideoClosed(@NonNull final String adUnitId) {}

        private void onRewardedVideoFailed(@NonNull final String errorMessage) {
            mLoadAdButton.setEnabled(true);
            mLoadAdButton.setText(getResources().getText(R.string.load_ad_button));

            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private class MoPubRewardedVideosClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mLoadAdButton.setEnabled(false);
            mLoadAdButton.setText(getResources().getText(R.string.start_load_ad_button));

            /*
             Replace AD_UNIT_ID with your unique Ad Unit ID.
             Please, read official documentation how to obtain one: {@link https://app.mopub.com}
             */
            MoPubRewardedVideos.loadRewardedVideo(AD_UNIT_ID);
        }
    };
}
