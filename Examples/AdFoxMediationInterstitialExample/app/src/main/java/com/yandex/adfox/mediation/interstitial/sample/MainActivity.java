/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.adfox.mediation.interstitial.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.InterstitialAd;
import com.yandex.mobile.ads.InterstitialEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AdRequest mAdRequest;
    private InterstitialAd mInterstitialAd;

    private Button mLoadInterstitialAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadInterstitialAdButton = (Button) findViewById(R.id.load_interstitial_button);
        mLoadInterstitialAdButton.setOnClickListener(mInterstitialClickListener);

        initInterstitialAd();
    }

    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);

        /*
        * Replace demo R-M-206876-12 with actual Block ID
        */
        mInterstitialAd.setBlockId("R-M-206876-12");

        mAdRequest = createAdRequest();
        mInterstitialAd.setInterstitialEventListener(mInterstitialAdEventListener);
    }

    private AdRequest createAdRequest() {
        /*
        * Replace demo MediationConfigurator.ADMOB_DEMO_INTERSTITIAL with actual AdFox parameters.
        * Following demo parameters may be used for testing:
        * Yandex: MediationConfigurator.ADFOX_DEMO_INTERSTITIAL
        * AdMob mediation: MediationConfigurator.ADMOB_DEMO_INTERSTITIAL
        * Facebook mediation: MediationConfigurator.FACEBOOK_DEMO_INTERSTITIAL
        * MoPub mediation: MediationConfigurator.MOPUB_DEMO_INTERSTITIAL
        * MyTarget mediation: MediationConfigurator.MYTARGET_DEMO_INTERSTITIAL
        * StartApp mediation: MediationConfigurator.STARTAPP_DEMO_INTERSTITIAL
        */
        return AdRequest.builder()
                .withParameters(MediationConfigurator.ADMOB_DEMO_INTERSTITIAL)
                .build();
    }

    @Override
    protected void onDestroy() {
        mInterstitialAd.setInterstitialEventListener(null);
        mInterstitialAd.destroy();
        super.onDestroy();
    }

    private View.OnClickListener mInterstitialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoadInterstitialAdButton.setEnabled(false);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.start_load_interstitial_button));

            mInterstitialAd.loadAd(mAdRequest);
        }
    };

    private InterstitialEventListener mInterstitialAdEventListener = new InterstitialEventListener.SimpleInterstitialEventListener() {

        @Override
        public void onInterstitialLoaded() {
            mInterstitialAd.show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }

        @Override
        public void onInterstitialFailedToLoad(final AdRequestError error) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad, error = " + error, Toast.LENGTH_SHORT).show();

            mLoadInterstitialAdButton.setEnabled(true);
            mLoadInterstitialAdButton.setText(getResources().getText(R.string.load_interstitial_button));
        }
    };
}
