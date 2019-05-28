/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.mopubadapter.banner.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mopub.mobileads.DefaultBannerAdListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private MoPubView mMoPubView;
    private MoPubInitializer mMoPubInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loadBannerButton = findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(mLoadBannerClickListener);

        mMoPubView = findViewById(R.id.mopub_view);
        mMoPubInitializer = new MoPubInitializer();
        initMoPubView();
    }

    private void initMoPubView() {
        /*
         * Replace AD_UNIT_ID with your unique Ad Unit ID.
         * Please, read official documentation how to obtain one: {@link https://app.mopub.com}
         */
        mMoPubView.setAdUnitId(AD_UNIT_ID);

        mMoPubView.setBannerAdListener(mBannerAdListener);
        mMoPubView.setAutorefreshEnabled(false);
    }

    private void refreshBannerAd() {
        mMoPubView.setVisibility(View.INVISIBLE);
        mMoPubInitializer.initializeSdk(this, AD_UNIT_ID, () -> {
            mMoPubView.loadAd();
        });
    }

    private View.OnClickListener mLoadBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }
    };

    MoPubView.BannerAdListener mBannerAdListener = new DefaultBannerAdListener() {
        @Override
        public void onBannerLoaded(MoPubView banner) {
            mMoPubView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
            super.onBannerFailed(banner, errorCode);
        }
    };

    @Override
    protected void onDestroy() {
        if (mMoPubView != null) {
            mMoPubView.destroy();
        }
        super.onDestroy();
    }
}
