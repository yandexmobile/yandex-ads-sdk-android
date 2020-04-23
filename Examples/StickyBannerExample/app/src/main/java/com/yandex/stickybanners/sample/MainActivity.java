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
import android.widget.RelativeLayout;
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

    private RelativeLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loadBannerButton = findViewById(R.id.load_banner_button);
        loadBannerButton.setOnClickListener(new LoadBannerClickListener());

        mRootLayout = findViewById(R.id.root_layout);

        mAdView = findViewById(R.id.banner_view);
        //Replace demo R-M-DEMO-adaptive-sticky with actual Block ID
        mAdView.setBlockId(DEMO_BLOCK_ID);

        mAdView.setAdEventListener(new StickyBannerEventListener());
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        final int containerWidth = getContainerWidth();
        mAdView.setAdSize(AdSize.stickySize(containerWidth));
        super.onWindowFocusChanged(hasFocus);
    }

    private int getContainerWidth() {
        float density = getResources().getDisplayMetrics().density;
        return (int)( mRootLayout.getWidth() / density);
    }

    private class StickyBannerEventListener extends AdEventListener.SimpleAdEventListener {
        @Override
        public void onAdLoaded() {
            mAdView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Ad loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            Toast.makeText(MainActivity.this, "Failed to load with reason: " +
                    error.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadBannerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            refreshBannerAd();
        }

        private void refreshBannerAd() {
            mAdView.setVisibility(View.GONE);
            final AdRequest adRequest = AdRequest.builder().build();
            mAdView.loadAd(adRequest);
        }
    }
}
