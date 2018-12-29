/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.mopub.mobileads;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;

import java.util.HashMap;
import java.util.Map;

public class YandexRewarded extends CustomEventRewardedVideo {

    private static final String TAG = "Yandex MoPub Adapter";

    private static final String AD_NETWORK_ID = "Yandex MoPub Rewarded Adapter";

    private static final String BLOCK_ID = "blockID";
    private static final String LOCATION = "location";
    private static final String OPEN_LINKS_IN_APP = "openLinksInApp";

    @NonNull
    private final ExtrasParser mExtrasParser;

    @NonNull
    private final AdRequestConfigurator mAdRequestConfigurator;

    @Nullable
    private RewardedAd mRewardedAd;

    public YandexRewarded() {
        mExtrasParser = new ExtrasParser();
        mAdRequestConfigurator = new AdRequestConfigurator(mExtrasParser);
    }

    @Override
    protected boolean hasVideoAvailable() {
        return mRewardedAd != null && mRewardedAd.isLoaded();
    }

    @Override
    protected void showVideo() {
        if (hasVideoAvailable()) {
            mRewardedAd.show();
        } else {
            Log.d(TAG, "Tried to show a Yandex Mobile Ads rewarded ad before it finished loading. " +
                    "Please try again.");
        }
    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @Override
    protected boolean checkAndInitializeSdk(@NonNull final Activity launcherActivity,
                                            @NonNull final Map<String, Object> localExtras,
                                            @NonNull final Map<String, String> serverExtras) {
        return true;
    }

    @Override
    protected void loadWithSdkInitialized(@NonNull final Activity activity,
                                          @NonNull final Map<String, Object> localExtras,
                                          @NonNull final Map<String, String> serverExtras) {
        final String blockId = mExtrasParser.parseBlockId(serverExtras);
        if (TextUtils.isEmpty(blockId) == false) {
            final boolean openLinksInApp = mExtrasParser.parseOpenLinksInApp(serverExtras);

            mRewardedAd = new RewardedAd(activity);
            mRewardedAd.setBlockId(blockId);
            mRewardedAd.shouldOpenLinksInApp(openLinksInApp);

            final MoPubRewardedAdEventListener moPubRewardedAdEventListener = new MoPubRewardedAdEventListener();
            mRewardedAd.setRewardedAdEventListener(moPubRewardedAdEventListener);

            final AdRequest adRequest = mAdRequestConfigurator.configureAdRequest(localExtras);
            mRewardedAd.loadAd(adRequest);
        } else {
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(YandexRewarded.class,
                    AD_NETWORK_ID, MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }
    }

    @NonNull
    @Override
    protected String getAdNetworkId() {
        return AD_NETWORK_ID;
    }

    @Override
    protected void onInvalidate() {
        if (mRewardedAd != null) {
            mRewardedAd.setRewardedAdEventListener(null);
            mRewardedAd.destroy();
            mRewardedAd = null;
        }
    }

    static class MoPubRewardedAdEventListener extends RewardedAdEventListener {

        @NonNull
        private final MoPubErrorCodeConverter mMoPubErrorCodeConverter;

        MoPubRewardedAdEventListener() {
            mMoPubErrorCodeConverter = new MoPubErrorCodeConverter();
        }

        @Override
        public void onAdClosed() {
            // do nothing
        }

        @Override
        public void onAdDismissed() {
            MoPubRewardedVideoManager.onRewardedVideoClosed(YandexRewarded.class, AD_NETWORK_ID);
        }

        @Override
        public void onAdFailedToLoad(final AdRequestError error) {
            final MoPubErrorCode mopubErrorCode = mMoPubErrorCodeConverter.convertToMoPubErrorCode(error);
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(YandexRewarded.class,
                    AD_NETWORK_ID, mopubErrorCode);
        }

        @Override
        public void onAdLeftApplication() {
            MoPubRewardedVideoManager.onRewardedVideoClicked(YandexRewarded.class, AD_NETWORK_ID);
        }

        @Override
        public void onAdLoaded() {
            MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(YandexRewarded.class, AD_NETWORK_ID);
        }

        @Override
        public void onAdOpened() {
            MoPubRewardedVideoManager.onRewardedVideoClicked(YandexRewarded.class, AD_NETWORK_ID);
        }

        @Override
        public void onAdShown() {
            MoPubRewardedVideoManager.onRewardedVideoStarted(YandexRewarded.class, AD_NETWORK_ID);
        }

        @Override
        public void onRewarded(@NonNull final Reward reward) {
            final MoPubReward moPubReward = MoPubReward.success(reward.getType(), reward.getAmount());
            MoPubRewardedVideoManager.onRewardedVideoCompleted(YandexRewarded.class,
                    AD_NETWORK_ID, moPubReward);
        }
    }

    static class MoPubErrorCodeConverter {

        @NonNull
        MoPubErrorCode convertToMoPubErrorCode(@NonNull final AdRequestError adRequestError) {
            final MoPubErrorCode mopubErrorCode;
            switch (adRequestError.getCode()) {
                case AdRequestError.Code.INTERNAL_ERROR:
                case AdRequestError.Code.SYSTEM_ERROR:
                    mopubErrorCode = MoPubErrorCode.INTERNAL_ERROR;
                    break;
                case AdRequestError.Code.INVALID_REQUEST:
                    mopubErrorCode = MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
                    break;
                case AdRequestError.Code.NETWORK_ERROR:
                    mopubErrorCode = MoPubErrorCode.NO_CONNECTION;
                    break;
                case AdRequestError.Code.NO_FILL:
                    mopubErrorCode = MoPubErrorCode.NO_FILL;
                    break;
                case AdRequestError.Code.UNKNOWN_ERROR:
                    mopubErrorCode = MoPubErrorCode.UNSPECIFIED;
                    break;
                default:
                    mopubErrorCode = MoPubErrorCode.NETWORK_NO_FILL;
                    break;
            }

            return mopubErrorCode;
        }
    }

    static class AdRequestConfigurator {

        private static final String MEDIATION_NETWORK_KEY = "mediation_network";
        private static final String MEDIATION_NETWORK = "mopub";

        @NonNull
        private final ExtrasParser mExtrasParser;

        AdRequestConfigurator(@NonNull final ExtrasParser extrasParser) {
            mExtrasParser = extrasParser;
        }

        @NonNull
        AdRequest configureAdRequest(@NonNull final Map<String, Object> localExtras) {
            final Map<String, String> adRequestParams = new HashMap<>();
            adRequestParams.put(MEDIATION_NETWORK_KEY, MEDIATION_NETWORK);

            final Location location = mExtrasParser.parseLocation(localExtras);
            return AdRequest.builder()
                    .withLocation(location)
                    .withParameters(adRequestParams)
                    .build();
        }
    }

    static class ExtrasParser {

        @Nullable
        Location parseLocation(@NonNull final Map<String, Object> localExtras) {
            final Object location = localExtras.get(LOCATION);
            return location instanceof Location ? (Location) location : null;
        }

        @Nullable
        String parseBlockId(@NonNull final Map<String, String> serverExtras) {
            return serverExtras.get(BLOCK_ID);
        }

        boolean parseOpenLinksInApp(@NonNull final Map<String, String> serverExtras) {
            return Boolean.parseBoolean(serverExtras.get(OPEN_LINKS_IN_APP));
        }
    }
}
