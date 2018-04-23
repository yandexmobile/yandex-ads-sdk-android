/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.admob.mobileads;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.HashMap;
import java.util.Map;

public class YandexBanner implements CustomEventBanner {

    private static final String TAG = "Yandex AdMob Adapter";

    private static final String BLOCK_ID = "blockID";
    private static final String AD_WIDTH_KEY = "adWidth";
    private static final String AD_HEIGHT_KEY = "adHeight";
    private static final String OPEN_LINKS_IN_APP = "openLinksInApp";

    private static final String MEDIATION_NETWORK_KEY = "mediation_network";
    private static final String MEDIATION_NETWORK = "admob";

    private String mServerAdWidth;
    private String mServerAdHeight;

    private AdView mAdView;

    private String mBlockId;
    private boolean mOpenLinksInApp;

    private CustomEventBannerListener mBannerListener;

    @Override
    public void requestBannerAd(final Context context, final CustomEventBannerListener customEventBannerListener,
                                final String serverParameter, final AdSize adMobAdSize,
                                final MediationAdRequest mediationAdRequest, final Bundle adMobExtras) {

        mBannerListener = customEventBannerListener;
        if (mBannerListener == null) {
            Log.w(TAG, "customEventBannerListener must not be null");
            return;
        }

        if (isValidServerExtras(serverParameter)) {
            try {
                parseServerExtras(serverParameter);

                final com.yandex.mobile.ads.AdSize adSize = calculateAdSize(adMobAdSize);
                final com.yandex.mobile.ads.AdRequest adRequest = configureAdRequest(mediationAdRequest);

                mAdView = new AdView(context);
                mAdView.setAdSize(adSize);
                mAdView.setBlockId(mBlockId);
                mAdView.shouldOpenLinksInApp(mOpenLinksInApp);
                mAdView.setAdEventListener(mAdEventListener);

                mAdView.loadAd(adRequest);
            } catch (JSONException e) {
                mBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            }
        } else {
            mBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
        }
    }

    private com.yandex.mobile.ads.AdRequest configureAdRequest(final MediationAdRequest mediationAdRequest) {
        final com.yandex.mobile.ads.AdRequest.Builder adRequestBuilder = com.yandex.mobile.ads.AdRequest.builder();
        final Map<String, String> adRequestParams = new HashMap<>();
        adRequestParams.put(MEDIATION_NETWORK_KEY, MEDIATION_NETWORK);
        adRequestBuilder.withParameters(adRequestParams);
        if (mediationAdRequest != null) {
            final Location location = mediationAdRequest.getLocation();
            adRequestBuilder.withLocation(location);

            final Set<String> mediationKeywords = mediationAdRequest.getKeywords();
            if (mediationKeywords != null) {
                final List<String> keywords = new ArrayList<>(mediationKeywords);
                adRequestBuilder.withContextTags(keywords);
            }
        }

        return adRequestBuilder.build();
    }

    private static boolean isValidServerExtras(final String serverParameter) {
        try {
            final JSONObject serverExtras = new JSONObject(serverParameter);
            return !TextUtils.isEmpty(serverExtras.getString(BLOCK_ID));
        } catch (JSONException e) {
            return false;
        }
    }

    private void parseServerExtras(final String serverParameter) throws JSONException {
        final JSONObject serverExtras = new JSONObject(serverParameter);

        mBlockId = serverExtras.getString(BLOCK_ID);
        mServerAdWidth = serverExtras.optString(AD_WIDTH_KEY);
        mServerAdHeight = serverExtras.optString(AD_HEIGHT_KEY);
        mOpenLinksInApp = Boolean.parseBoolean(serverExtras.optString(OPEN_LINKS_IN_APP));
    }

    private com.yandex.mobile.ads.AdSize calculateAdSize(final AdSize adMobAdSize) {
        try {
            final int serverAdWidth = Integer.parseInt(mServerAdWidth);
            final int serverAdHeight = Integer.parseInt(mServerAdHeight);

            return new com.yandex.mobile.ads.AdSize(serverAdWidth, serverAdHeight);
        } catch (NumberFormatException exception) {
            final int adMobWidth = adMobAdSize != null ? adMobAdSize.getWidth() : 0;
            final int adMobHeight = adMobAdSize != null ? adMobAdSize.getHeight() : 0;

            return new com.yandex.mobile.ads.AdSize(adMobWidth, adMobHeight);
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
            mAdView = null;
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    private AdEventListener mAdEventListener = new AdEventListener() {

        @Override
        public void onAdClosed() {
            mBannerListener.onAdClosed();
        }

        @Override
        public void onAdFailedToLoad(final AdRequestError adRequestError) {
            final int admobErrorCode;
            switch (adRequestError.getCode()) {
                case AdRequestError.Code.INTERNAL_ERROR :
                case AdRequestError.Code.SYSTEM_ERROR :
                    admobErrorCode = AdRequest.ERROR_CODE_INTERNAL_ERROR;
                    break;
                case AdRequestError.Code.INVALID_REQUEST :
                    admobErrorCode = AdRequest.ERROR_CODE_INVALID_REQUEST;
                    break;
                case AdRequestError.Code.NETWORK_ERROR :
                    admobErrorCode = AdRequest.ERROR_CODE_NETWORK_ERROR;
                    break;
                case AdRequestError.Code.NO_FILL :
                    admobErrorCode = AdRequest.ERROR_CODE_NO_FILL;
                    break;
                default:
                    admobErrorCode = AdRequest.ERROR_CODE_NO_FILL;
                    break;
            }

            mBannerListener.onAdFailedToLoad(admobErrorCode);
        }

        @Override
        public void onAdLeftApplication() {
            mBannerListener.onAdClicked();
            mBannerListener.onAdLeftApplication();
            mBannerListener.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            mBannerListener.onAdLoaded(mAdView);
        }

        @Override
        public void onAdOpened() {
            mBannerListener.onAdClicked();
            mBannerListener.onAdOpened();
        }
    };
}
