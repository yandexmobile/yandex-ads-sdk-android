/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.adfox.mediation.banner.sample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private static final String ADFOX_BLOCK_ID = "adf-279013/966631";
    private static final String ADMOB_BLOCK_ID = "adf-279013/975926";
    private static final String APPLOVIN_BLOCK_ID = "adf-279013/1049726";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/975929";
    private static final String MOPUB_BLOCK_ID = "adf-279013/975927";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/975928";
    private static final String STARTAPP_BLOCK_ID = "adf-279013/1004807";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loadBannerButton = (Button) findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(mLoadBannerClickListener);

        mAdView = (AdView) findViewById(R.id.banner_view);
        initBannerView();
    }

    private void initBannerView() {
        /*
         * Replace demo BLOCK_ID with actual Block ID
         * Following demo block ids may be used for testing:
         * Yandex: ADFOX_BLOCK_ID
         * AdMob mediation: ADMOB_BLOCK_ID
         * AppLovin mediation: APPLOVIN_BLOCK_ID
         * Facebook mediation: FACEBOOK_BLOCK_ID
         * MoPub mediation: MOPUB_BLOCK_ID
         * MyTarget mediation: MYTARGET_BLOCK_ID
         * StartApp mediation: STARTAPP_BLOCK_ID
         */
        mAdView.setBlockId(ADMOB_BLOCK_ID);
        mAdView.setAdSize(AdSize.BANNER_320x50);
        mAdView.setAdEventListener(mBannerAdEventListener);
    }

    private View.OnClickListener mLoadBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }
    };

    private void refreshBannerAd() {
        mAdView.setVisibility(View.INVISIBLE);
        mAdView.loadAd(AdRequest.builder().build());
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.setAdEventListener(null);
            mAdView.destroy();
        }

        super.onDestroy();
    }

    private AdEventListener mBannerAdEventListener = new AdEventListener.SimpleAdEventListener() {
        @Override
        public void onAdLoaded() {
            mAdView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad, error = " + error, Toast.LENGTH_SHORT).show();
        }
    };
}
