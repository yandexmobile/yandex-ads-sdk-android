/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.adfox.mediation.banner.sample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

import java.util.Map;

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
        * Replace demo R-M-206876-13 with actual Block ID
        */
        mAdView.setBlockId("R-M-206876-13");
        mAdView.setAdSize(AdSize.BANNER_320x50);

        mAdRequest = createAdRequest();
        mAdView.setAdEventListener(mBannerAdEventListener);
    }

    private AdRequest createAdRequest() {
        /*
        * Replace demo MediationConfigurator.ADMOB_DEMO_BANNER with actual AdFox parameters.
        * Following demo parameters may be used for testing:
        * Yandex: MediationConfigurator.ADFOX_DEMO_BANNER
        * AdMob mediation: MediationConfigurator.ADMOB_DEMO_BANNER
        * Facebook mediation: MediationConfigurator.FACEBOOK_DEMO_BANNER
        * MoPub mediation: MediationConfigurator.MOPUB_DEMO_BANNER
        * MyTarget mediation: MediationConfigurator.MYTARGET_DEMO_BANNER
        * StartApp mediation: MediationConfigurator.STARTAPP_DEMO_BANNER
        */
        return AdRequest.builder()
                .withParameters(MediationConfigurator.ADMOB_DEMO_BANNER)
                .build();
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
