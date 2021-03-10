/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2020 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.stickybanners.sample;

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

    private static final String DEMO_BLOCK_ID = "R-M-DEMO-adaptive-sticky";

    private BannerAdView mBannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBannerAdView = findViewById(R.id.ad_view);
        initAdView();

        final Button loadBannerButton = findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(new LoadBannerClickListener());
    }

    private void initAdView() {
        // You can use width of your adview container instead of AdSize.FULL_WIDTH
        mBannerAdView.setAdSize(AdSize.stickySize(AdSize.FULL_WIDTH));

        // Replace demo R-M-DEMO-adaptive-sticky with actual Block ID
        mBannerAdView.setBlockId(DEMO_BLOCK_ID);
        mBannerAdView.setBannerAdEventListener(new StickyBannerEventListener());
    }


    private class LoadBannerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }

        private void refreshBannerAd() {
            final AdRequest adRequest = new AdRequest.Builder().build();
            mBannerAdView.loadAd(adRequest);
        }
    }

    private class StickyBannerEventListener implements BannerAdEventListener {

        @Override
        public void onAdLoaded() {
            Toast.makeText(MainActivity.this, "Ad loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
            Toast.makeText(MainActivity.this, "Failed to load with reason: " +
                    adRequestError.getDescription(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLeftApplication() {
            Toast.makeText(MainActivity.this, "onLeftApplication", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReturnedToApplication() {
            Toast.makeText(MainActivity.this, "onReturnedToApplication", Toast.LENGTH_SHORT).show();
        }
    }
}
