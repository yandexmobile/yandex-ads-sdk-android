/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2016 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.InterstitialAd;
import com.yandex.mobile.ads.InterstitialEventListener;

import java.util.Map;

public class YandexInterstitial extends CustomEventInterstitial {

    private static final String TAG = "Yandex MoPub Adapter";

    private static final String BLOCK_ID = "blockID";
    private static final String LOCATION = "location";
    private static final String OPEN_LINKS_IN_APP = "openLinksInApp";

    private String mBlockId;
    private Location mLocation;
    private boolean mOpenLinksInApp;

    private InterstitialAd mInterstitialAd;
    private CustomEventInterstitialListener mInterstitialListener;

    @Override
    protected void loadInterstitial(final Context context,
                                    final CustomEventInterstitialListener customEventInterstitialListener,
                                    final Map<String, Object> localExtras,
                                    final Map<String, String> serverExtras) {

        mInterstitialListener = customEventInterstitialListener;
        if (mInterstitialListener == null) {
            Log.w(TAG, "customEventInterstitialListener must not be null");
            return;
        }

        if (isValidLocalExtras(localExtras) && isValidServerExtras(serverExtras)) {
            parseLocalExtras(localExtras);
            parseServerExtras(serverExtras);

            final AdRequest adRequest = AdRequest.builder().withLocation(mLocation).build();

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setBlockId(mBlockId);
            mInterstitialAd.shouldOpenLinksInApp(mOpenLinksInApp);
            mInterstitialAd.setInterstitialEventListener(mInterstitialEventListener);

            mInterstitialAd.loadAd(adRequest);
        } else {
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }
    }

    private boolean isValidLocalExtras(Map<String, Object> localExtras) {
        return localExtras != null &&
                (localExtras.get(LOCATION) == null || localExtras.get(LOCATION) instanceof Location);
    }

    private boolean isValidServerExtras(final Map<String, String> serverExtras) {
        return serverExtras != null && !TextUtils.isEmpty(serverExtras.get(BLOCK_ID));
    }

    private void parseLocalExtras(final Map<String, Object> localExtras) {
        mLocation = (Location) localExtras.get(LOCATION);
    }

    private void parseServerExtras(final Map<String, String> serverExtras) {
        mBlockId = serverExtras.get(BLOCK_ID);
        mOpenLinksInApp = Boolean.parseBoolean(serverExtras.get(OPEN_LINKS_IN_APP));
    }

    @Override
    protected void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "Tried to show a MobMetricaAds interstitial ad before it finished loading. Please try again.");
        }
    }

    @Override
    protected void onInvalidate() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
            mInterstitialAd = null;
        }
    }

    private InterstitialEventListener mInterstitialEventListener = new InterstitialEventListener.SimpleInterstitialEventListener() {

        @Override
        public void onInterstitialDismissed() {
            mInterstitialListener.onInterstitialDismissed();
        }

        @Override
        public void onInterstitialFailedToLoad(final AdRequestError adRequestError) {
            final MoPubErrorCode mopubErrorCode;
            switch (adRequestError.getCode()) {
                case AdRequestError.Code.INTERNAL_ERROR :
                case AdRequestError.Code.SYSTEM_ERROR :
                    mopubErrorCode = MoPubErrorCode.INTERNAL_ERROR;
                    break;
                case AdRequestError.Code.INVALID_REQUEST :
                    mopubErrorCode = MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
                    break;
                case AdRequestError.Code.NETWORK_ERROR :
                    mopubErrorCode = MoPubErrorCode.NO_CONNECTION;
                    break;
                case AdRequestError.Code.NO_FILL :
                    mopubErrorCode = MoPubErrorCode.NO_FILL;
                    break;
                case AdRequestError.Code.UNKNOWN_ERROR :
                    mopubErrorCode = MoPubErrorCode.UNSPECIFIED;
                    break;
                default:
                    mopubErrorCode = MoPubErrorCode.NETWORK_NO_FILL;
                    break;
            }

            mInterstitialListener.onInterstitialFailed(mopubErrorCode);
        }

        @Override
        public void onInterstitialLoaded() {
            mInterstitialListener.onInterstitialLoaded();
        }

        @Override
        public void onInterstitialShown() {
            mInterstitialListener.onInterstitialShown();
        }

        @Override
        public void onAdLeftApplication() {
            mInterstitialListener.onLeaveApplication();
        }

        @Override
        public void onAdOpened() {
            mInterstitialListener.onInterstitialClicked();
        }
    };
}
