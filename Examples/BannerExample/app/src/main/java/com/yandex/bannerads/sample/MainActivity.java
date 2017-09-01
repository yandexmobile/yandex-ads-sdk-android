/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.bannerads.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private AdRequest mAdRequest;

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
        * Replace demo R-M-DEMO-320x50 with actual Block ID
        * Following demo Block IDs may be used for testing:
        * R-M-DEMO-300x250 for AdSize.BANNER_300x250
        * R-M-DEMO-300x250-context for AdSize.BANNER_300x250
        * R-M-DEMO-300x300-context for AdSize.BANNER_300x300
        * R-M-DEMO-320x50 for AdSize.BANNER_320x50
        * R-M-DEMO-320x50-app_install for AdSize.BANNER_320x50
		* R-M-DEMO-320x100-context for AdSize.BANNER_320x100
        * R-M-DEMO-728x90 for AdSize.BANNER_728x90
        */
        mAdView.setBlockId("R-M-DEMO-320x50");
        mAdView.setAdSize(AdSize.BANNER_320x50);

        mAdRequest = AdRequest.builder().build();
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
        mAdView.loadAd(mAdRequest);
    }

    private AdEventListener mBannerAdEventListener = new AdEventListener.SimpleAdEventListener() {
        @Override
        public void onAdLoaded() {
            mAdView.setVisibility(View.VISIBLE);
        }
    };
}
