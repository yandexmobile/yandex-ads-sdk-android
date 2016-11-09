/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2016 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.admobadapter.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdRequest;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY";

    private AdView mAdMobView;
    private AdRequest mAdRequest;
    private RelativeLayout mAdViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loadBannerButton = (Button) findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(mLoadBannerClickListener);

        mAdViewContainer = (RelativeLayout) findViewById(R.id.container);
        initAdMobView();
    }

    private void initAdMobView() {
        mAdMobView = new AdView(this);
        mAdMobView.setAdSize(AdSize.BANNER);

        /*
          Replace AD_UNIT_ID with your unique Ad Unit ID.
          Please, read official documentation how to obtain one: https://apps.admob.com
        */
        mAdMobView.setAdUnitId(AD_UNIT_ID);
        mAdMobView.setAdListener(mBannerAdListener);

        mAdRequest = new AdRequest.Builder().build();

        final LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mAdViewContainer.addView(mAdMobView, layoutParams);
    }

    private void refreshBannerAd() {
        mAdMobView.setVisibility(View.INVISIBLE);
        mAdMobView.loadAd(mAdRequest);
    }

    private View.OnClickListener mLoadBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }
    };

    private AdListener mBannerAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            mAdMobView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(final int errorCode) {
            super.onAdFailedToLoad(errorCode);
        }
    };

    @Override
    protected void onDestroy() {
        if (mAdMobView != null) {
            mAdMobView.destroy();
        }
        super.onDestroy();
    }
}
