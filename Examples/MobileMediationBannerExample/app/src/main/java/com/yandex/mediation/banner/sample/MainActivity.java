/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mediation.banner.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;

public class MainActivity extends AppCompatActivity {

    private static final String YANDEX_BLOCK_ID = "adf-279013/966631";
    private static final String ADCOLONY_BLOCK_ID = "adf-279013/1196978";
    private static final String ADMOB_BLOCK_ID = "adf-279013/975926";
    private static final String APPLOVIN_BLOCK_ID = "adf-279013/1049726";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/975929";
    private static final String CHARTBOOST_BLOCK_ID = "adf-279013/1197235";
    private static final String MOPUB_BLOCK_ID = "adf-279013/975927";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/975928";
    private static final String STARTAPP_BLOCK_ID = "adf-279013/1004807";
    private static final String VUNGLE_BLOCK_ID = "adf-279013/1198306";

    private BannerAdView mBannerAdView;

    private Spinner mSpinner;

    private RelativeLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = findViewById(R.id.mediation_spinner);
        mRootLayout = findViewById(R.id.root_layout);

        final Button loadBannerButton = (Button) findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(mLoadBannerClickListener);
    }

    @Override
    protected void onDestroy() {
        destroyBannerAdView();
        super.onDestroy();
    }

    private void refreshBannerAd() {
        destroyBannerAdView();
        createBannerAdView();
        initBannerAdView();
        mBannerAdView.setVisibility(View.INVISIBLE);
        mBannerAdView.loadAd(new AdRequest.Builder().build());
    }

    private void destroyBannerAdView() {
        if (mBannerAdView != null) {
            mRootLayout.removeView(mBannerAdView);
            mBannerAdView.setBannerAdEventListener(null);
            mBannerAdView.destroy();
            mBannerAdView = null;
        }
    }

    private void createBannerAdView() {
        mBannerAdView = new BannerAdView(this);

        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.alignWithParent = true;
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mRootLayout.addView(mBannerAdView, layoutParams);
    }

    private void initBannerAdView() {
        final String blockId = getBlockId();
        mBannerAdView.setBlockId(blockId);
        mBannerAdView.setAdSize(AdSize.BANNER_320x50);
        mBannerAdView.setBannerAdEventListener(mBannerAdEventListener);
    }

    private String getBlockId() {
        /*
         * Replace demo BLOCK_ID with actual Block ID
         * Following demo block ids may be used for testing:
         * Yandex: YANDEX_BLOCK_ID
         * AdMob mediation: ADMOB_BLOCK_ID
         * AppLovin mediation: APPLOVIN_BLOCK_ID
         * Facebook mediation: FACEBOOK_BLOCK_ID
         * MoPub mediation: MOPUB_BLOCK_ID
         * MyTarget mediation: MYTARGET_BLOCK_ID
         * StartApp mediation: STARTAPP_BLOCK_ID
         */
        final String mediation = mSpinner.getSelectedItem().toString();
        switch (mediation) {
            case "Yandex":
                return YANDEX_BLOCK_ID;
            case "Adcolony":
                return ADCOLONY_BLOCK_ID;
            case "AdMob":
                return ADMOB_BLOCK_ID;
            case "AppLovin":
                return APPLOVIN_BLOCK_ID;
            case "Chartboost":
                return CHARTBOOST_BLOCK_ID;
            case "Facebook":
                return FACEBOOK_BLOCK_ID;
            case "MoPub":
                return MOPUB_BLOCK_ID;
            case "MyTarget":
                return MYTARGET_BLOCK_ID;
            case "StartApp":
                return STARTAPP_BLOCK_ID;
            case "Vungle":
                return VUNGLE_BLOCK_ID;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final View.OnClickListener mLoadBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }
    };

    private final BannerAdEventListener mBannerAdEventListener = new BannerAdEventListener() {

        @Override
        public void onAdLoaded() {
            mBannerAdView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad, error = " + error, Toast.LENGTH_SHORT).show();
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
