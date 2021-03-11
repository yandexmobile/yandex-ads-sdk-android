/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.bannerads.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;

public class MainActivity extends AppCompatActivity {

    private BannerAdView mBannerAdView;
    private AdRequest mAdRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loadBannerButton = (Button) findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(mLoadBannerClickListener);

        mBannerAdView = (BannerAdView) findViewById(R.id.banner_view);
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
        mBannerAdView.setBlockId("R-M-DEMO-320x50");
        mBannerAdView.setAdSize(AdSize.BANNER_320x50);

        mAdRequest = new AdRequest.Builder().build();
        mBannerAdView.setBannerAdEventListener(mBannerAdEventListener);
    }

    private final View.OnClickListener mLoadBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }
    };

    private void refreshBannerAd() {
        mBannerAdView.setVisibility(View.INVISIBLE);
        mBannerAdView.loadAd(mAdRequest);
    }

    private final BannerAdEventListener mBannerAdEventListener = new BannerAdEventListener() {

        @Override
        public void onAdLoaded() {
            Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
            mBannerAdView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
            Toast.makeText(MainActivity.this, adRequestError.getDescription(), Toast.LENGTH_SHORT).show();
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
