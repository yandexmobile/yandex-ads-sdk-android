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

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private static final String YANDEX_BLOCK_ID = "adf-279013/966631";
    private static final String ADMOB_BLOCK_ID = "adf-279013/975926";
    private static final String APPLOVIN_BLOCK_ID = "adf-279013/1049726";
    private static final String FACEBOOK_BLOCK_ID = "adf-279013/975929";
    private static final String MOPUB_BLOCK_ID = "adf-279013/975927";
    private static final String MYTARGET_BLOCK_ID = "adf-279013/975928";
    private static final String STARTAPP_BLOCK_ID = "adf-279013/1004807";

    private AdView mAdView;

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
        mAdView.setVisibility(View.INVISIBLE);
        mAdView.loadAd(AdRequest.builder().build());
    }

    private void destroyBannerAdView() {
        if (mAdView != null) {
            mRootLayout.removeView(mAdView);
            mAdView.setAdEventListener(null);
            mAdView.destroy();
            mAdView = null;
        }
    }

    private void createBannerAdView() {
        mAdView = new AdView(this);

        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.alignWithParent = true;
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mRootLayout.addView(mAdView, layoutParams);
    }

    private void initBannerAdView() {
        final String blockId = getBlockId();
        mAdView.setBlockId(blockId);
        mAdView.setAdSize(AdSize.BANNER_320x50);
        mAdView.setAdEventListener(mBannerAdEventListener);
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
            case "AdMob":
                return ADMOB_BLOCK_ID;
            case "AppLovin":
                return APPLOVIN_BLOCK_ID;
            case "Facebook":
                return FACEBOOK_BLOCK_ID;
            case "MoPub":
                return MOPUB_BLOCK_ID;
            case "MyTarget":
                return MYTARGET_BLOCK_ID;
            case "StartApp":
                return STARTAPP_BLOCK_ID;
            default:
                throw new IllegalArgumentException();
        }
    }

    private View.OnClickListener mLoadBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }
    };

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
