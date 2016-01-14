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

import com.mopub.common.DataKeys;
import com.mopub.common.util.Views;
import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

import java.util.Map;

public class YandexBanner extends CustomEventBanner {

    private static final String TAG = "Yandex MoPub Adapter";

    private static final String BLOCK_ID = "blockID";
    private static final String LOCATION = "location";
    private static final String AD_WIDTH_KEY = "adWidth";
    private static final String AD_HEIGHT_KEY = "adHeight";
    private static final String OPEN_LINKS_IN_APP = "openLinksInApp";

    private AdView mAdView;

    private int mLocalAdWidth;
    private int mLocalAdHeight;
    private String mServerAdWidth;
    private String mServerAdHeight;

    private String mBlockId;
    private Location mLocation;
    private boolean mOpenLinksInApp;

    private CustomEventBannerListener mBannerListener;

    @Override
    protected void loadBanner(final Context context,
          final CustomEventBannerListener customEventBannerListener,
          final Map<String, Object> localExtras,
          final Map<String, String> serverExtras) {

        mBannerListener = customEventBannerListener;
        if (mBannerListener == null) {
            Log.w(TAG, "customEventBannerListener must not be null");
            return;
        }

        if (isValidLocalExtras(localExtras) && isValidServerExtras(serverExtras)) {
            parseLocalExtras(localExtras);
            parseServerExtras(serverExtras);

            final AdSize adSize = calculateAdSize();
            final AdRequest adRequest = AdRequest.builder().withLocation(mLocation).build();

            mAdView = new AdView(context);
            mAdView.setAdSize(adSize);
            mAdView.setBlockId(mBlockId);
            mAdView.shouldOpenLinksInApp(mOpenLinksInApp);
            mAdView.setAdEventListener(mAdEventListener);

            mAdView.loadAd(adRequest);
        } else {
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }
    }

    private static boolean isValidLocalExtras(final Map<String, Object> localExtras) {
        return localExtras != null &&
                localExtras.get(DataKeys.AD_WIDTH) instanceof Integer &&
                localExtras.get(DataKeys.AD_HEIGHT) instanceof Integer &&
                (localExtras.get(LOCATION) == null || localExtras.get(LOCATION) instanceof Location);
    }

    private static boolean isValidServerExtras(final Map<String, String> serverExtras) {
        return serverExtras != null && !TextUtils.isEmpty(serverExtras.get(BLOCK_ID));
    }

    private void parseLocalExtras(final Map<String, Object> localExtras) {
        mLocalAdWidth = (int) localExtras.get(DataKeys.AD_WIDTH);
        mLocalAdHeight = (int) localExtras.get(DataKeys.AD_HEIGHT);
        mLocation = (Location) localExtras.get(LOCATION);
    }

    private void parseServerExtras(final Map<String, String> serverExtras) {
        mBlockId = serverExtras.get(BLOCK_ID);
        mServerAdWidth = serverExtras.get(AD_WIDTH_KEY);
        mServerAdHeight = serverExtras.get(AD_HEIGHT_KEY);
        mOpenLinksInApp = Boolean.parseBoolean(serverExtras.get(OPEN_LINKS_IN_APP));
    }

    private AdSize calculateAdSize() {
        try {
            final int adWidth = Integer.parseInt(mServerAdWidth);
            final int adHeight = Integer.parseInt(mServerAdHeight);

            return new AdSize(adWidth, adHeight);
        } catch (NumberFormatException exception) {
            return new AdSize(mLocalAdWidth, mLocalAdHeight);
        }
    }

    @Override
    protected void onInvalidate() {
        if (mAdView != null) {
            Views.removeFromParent(mAdView);
            mAdView.destroy();
            mAdView = null;
        }
    }

    AdEventListener mAdEventListener = new AdEventListener() {
        @Override
        public void onAdClosed() {
            mBannerListener.onBannerCollapsed();
        }

        @Override
        public void onAdFailedToLoad(final AdRequestError adRequestError) {
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

            mBannerListener.onBannerFailed(mopubErrorCode);
        }

        @Override
        public void onAdLeftApplication() {
            mBannerListener.onLeaveApplication();
        }

        @Override
        public void onAdLoaded() {
            mBannerListener.onBannerLoaded(mAdView);
        }

        @Override
        public void onAdOpened() {
            mBannerListener.onBannerClicked();
            mBannerListener.onBannerExpanded();
        }
    };
}
