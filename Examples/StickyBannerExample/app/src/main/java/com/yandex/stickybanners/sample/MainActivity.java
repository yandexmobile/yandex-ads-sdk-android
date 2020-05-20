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

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private static final String DEMO_BLOCK_ID = "R-M-DEMO-adaptive-sticky";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = findViewById(R.id.ad_view);
        initAdView();

        final Button loadBannerButton = findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(new LoadBannerClickListener());
    }

    private void initAdView() {
        // You can use width of your adview container instead of AdSize.FULL_WIDTH
        mAdView.setAdSize(AdSize.stickySize(AdSize.FULL_WIDTH));

        // Replace demo R-M-DEMO-adaptive-sticky with actual Block ID
        mAdView.setBlockId(DEMO_BLOCK_ID);
        mAdView.setAdEventListener(new StickyBannerEventListener());
    }


    private class LoadBannerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }

        private void refreshBannerAd() {
            final AdRequest adRequest = AdRequest.builder().build();
            mAdView.loadAd(adRequest);
        }
    }

    private class StickyBannerEventListener extends AdEventListener.SimpleAdEventListener {
        @Override
        public void onAdLoaded() {
            Toast.makeText(MainActivity.this, "Ad loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            Toast.makeText(MainActivity.this, "Failed to load with reason: " +
                    error.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
