/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.admob.mobileads;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.OnContextChangedListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YandexRewarded implements MediationRewardedVideoAdAdapter, OnContextChangedListener {

    private static final String TAG = "Yandex AdMob Adapter";

    @NonNull
    private final AdRequestConfigurator mAdRequestConfigurator;

    @NonNull
    private final ServerExtrasParser mServerExtrasParser;

    @Nullable
    private RewardedAd mRewardedAd;

    @Nullable
    private Context mContext;

    @Nullable
    private MediationRewardedAdEventListener mMediationRewardedAdEventListener;

    private boolean mInitialized;

    public YandexRewarded() {
        mAdRequestConfigurator = new AdRequestConfigurator();
        mServerExtrasParser = new ServerExtrasParser();
    }

    @Override
    public void initialize(@Nullable final Context context,
                           @Nullable final MediationAdRequest mediationAdRequest,
                           @Nullable final String unused,
                           @Nullable final MediationRewardedVideoAdListener rewardedVideoAdListener,
                           @Nullable final Bundle serverParameters,
                           @Nullable final Bundle mediationExtras) {
        if (rewardedVideoAdListener == null) {
            Log.w(TAG, "MediationRewardedVideoAdListener must not be null");
            return;
        }

        if (context instanceof Activity == false) {
            Log.d(TAG, "Yandex Mobile Ads SDK requires an Activity context to initialize");
            rewardedVideoAdListener.onInitializationFailed(this, AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }

        final String blockId = mServerExtrasParser.parseBlockId(serverParameters);
        if (TextUtils.isEmpty(blockId) == false) {
            mContext = context;
            mInitialized = true;
            mMediationRewardedAdEventListener = new MediationRewardedAdEventListener(this, rewardedVideoAdListener);

            rewardedVideoAdListener.onInitializationSucceeded(this);
        } else {
            Log.w(TAG, "BlockId must not be empty");
            rewardedVideoAdListener.onInitializationFailed(this, AdRequest.ERROR_CODE_INVALID_REQUEST);
        }
    }

    @Override
    public void loadAd(@Nullable final MediationAdRequest mediationAdRequest,
                       @Nullable final Bundle serverParameters,
                       @Nullable final Bundle mediationExtras) {
        final String blockId = mServerExtrasParser.parseBlockId(serverParameters);
        if (TextUtils.isEmpty(blockId) == false
                && mContext != null
                && mMediationRewardedAdEventListener != null) {
            final boolean shouldOpenLinksInApp = mServerExtrasParser.parseShouldOpenLinksInApp(serverParameters);
            final com.yandex.mobile.ads.AdRequest adRequest = mAdRequestConfigurator.configureAdRequest(mediationAdRequest);

            mRewardedAd = new RewardedAd(mContext);
            mRewardedAd.setBlockId(blockId);
            mRewardedAd.shouldOpenLinksInApp(shouldOpenLinksInApp);
            mRewardedAd.setRewardedAdEventListener(mMediationRewardedAdEventListener);

            mRewardedAd.loadAd(adRequest);
        } else {
            if (mMediationRewardedAdEventListener != null) {
                final AdRequestError adRequestError = new AdRequestError(
                        AdRequestError.Code.INVALID_REQUEST, "invalid request");
                mMediationRewardedAdEventListener.onAdFailedToLoad(adRequestError);
            } else {
                Log.w(TAG, "Tried to load a Yandex Mobile Ads SDK rewarded Ad with invalid request.");
            }
        }
    }

    @Override
    public void showVideo() {
        if (mRewardedAd != null && mRewardedAd.isLoaded()) {
            mRewardedAd.show();
        } else {
            Log.d(TAG, "Tried to show a Yandex Mobile Ads SDK rewarded ad before it finished loading. " +
                    "Please try again.");
        }
    }

    @Override
    public boolean isInitialized() {
        return mInitialized;
    }

    @Override
    public void onDestroy() {
        if (mRewardedAd != null) {
            mRewardedAd.setRewardedAdEventListener(null);
            mRewardedAd.destroy();
            mRewardedAd = null;
        }

        mContext = null;
        mMediationRewardedAdEventListener = null;
    }

    @Override
    public void onContextChanged(@Nullable final Context context) {
        mContext = context;
    }

    @Override
    public void onPause() {
        //do nothing.
    }

    @Override
    public void onResume() {
        //do nothing.
    }

    static class AdMobReward implements RewardItem {

        @NonNull
        private final Reward mReward;

        AdMobReward(@NonNull final Reward reward) {
            mReward = reward;
        }

        @Override
        public String getType() {
            return mReward.getType();
        }

        @Override
        public int getAmount() {
            return mReward.getAmount();
        }
    }

    static class AdRequestConfigurator {

        private static final String MEDIATION_NETWORK_KEY = "mediation_network";
        private static final String MEDIATION_NETWORK = "admob";

        @NonNull
        com.yandex.mobile.ads.AdRequest configureAdRequest(@Nullable final MediationAdRequest mediationAdRequest) {
            final Map<String, String> adRequestParams = new HashMap<>();
            adRequestParams.put(MEDIATION_NETWORK_KEY, MEDIATION_NETWORK);
            final com.yandex.mobile.ads.AdRequest.Builder adRequestBuilder =
                    com.yandex.mobile.ads.AdRequest.builder().withParameters(adRequestParams);

            if (mediationAdRequest != null) {
                final Location location = mediationAdRequest.getLocation();
                if (location != null) {
                    adRequestBuilder.withLocation(location);
                }

                final Set<String> mediationKeywords = mediationAdRequest.getKeywords();
                if (mediationKeywords != null) {
                    final List<String> keywords = new ArrayList<>(mediationKeywords);
                    adRequestBuilder.withContextTags(keywords);
                }
            }

            return adRequestBuilder.build();
        }
    }

    static class MediationRewardedAdEventListener extends RewardedAdEventListener {

        @NonNull
        private final MediationErrorConverter mMediationErrorConverter;

        @NonNull
        private final MediationRewardedVideoAdAdapter mMediationRewardedVideoAdAdapter;

        @NonNull
        private final MediationRewardedVideoAdListener mMediationRewardedVideoAdListener;

        MediationRewardedAdEventListener(@NonNull final MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter,
                                         @NonNull final MediationRewardedVideoAdListener mediationRewardedVideoAdListener) {
            mMediationRewardedVideoAdAdapter = mediationRewardedVideoAdAdapter;
            mMediationRewardedVideoAdListener = mediationRewardedVideoAdListener;

            mMediationErrorConverter = new MediationErrorConverter();
        }

        @Override
        public void onAdClosed() {
            //do nothing.
        }

        @Override
        public void onAdDismissed() {
            mMediationRewardedVideoAdListener.onAdClosed(mMediationRewardedVideoAdAdapter);
        }

        @Override
        public void onAdFailedToLoad(final AdRequestError error) {
            final int adMobErrorCode = mMediationErrorConverter.convertToAdMobErrorCode(error);
            mMediationRewardedVideoAdListener.onAdFailedToLoad(mMediationRewardedVideoAdAdapter, adMobErrorCode);
        }

        @Override
        public void onAdLeftApplication() {
            mMediationRewardedVideoAdListener.onAdClicked(mMediationRewardedVideoAdAdapter);
            mMediationRewardedVideoAdListener.onAdLeftApplication(mMediationRewardedVideoAdAdapter);
        }

        @Override
        public void onAdLoaded() {
            mMediationRewardedVideoAdListener.onAdLoaded(mMediationRewardedVideoAdAdapter);
        }

        @Override
        public void onAdOpened() {
            mMediationRewardedVideoAdListener.onAdClicked(mMediationRewardedVideoAdAdapter);
        }

        @Override
        public void onAdShown() {
            mMediationRewardedVideoAdListener.onAdOpened(mMediationRewardedVideoAdAdapter);
            mMediationRewardedVideoAdListener.onVideoStarted(mMediationRewardedVideoAdAdapter);
        }

        @Override
        public void onRewarded(@NonNull final Reward reward) {
            final RewardItem rewardItem = new AdMobReward(reward);
            mMediationRewardedVideoAdListener.onRewarded(mMediationRewardedVideoAdAdapter, rewardItem);
        }
    }

    static class MediationErrorConverter {

        int convertToAdMobErrorCode(@NonNull final AdRequestError adRequestError) {
            final int adMobErrorCode;
            switch (adRequestError.getCode()) {
                case AdRequestError.Code.INTERNAL_ERROR:
                case AdRequestError.Code.SYSTEM_ERROR:
                    adMobErrorCode = AdRequest.ERROR_CODE_INTERNAL_ERROR;
                    break;
                case AdRequestError.Code.INVALID_REQUEST:
                    adMobErrorCode = AdRequest.ERROR_CODE_INVALID_REQUEST;
                    break;
                case AdRequestError.Code.NETWORK_ERROR:
                    adMobErrorCode = AdRequest.ERROR_CODE_NETWORK_ERROR;
                    break;
                case AdRequestError.Code.NO_FILL:
                    adMobErrorCode = AdRequest.ERROR_CODE_NO_FILL;
                    break;
                default:
                    adMobErrorCode = AdRequest.ERROR_CODE_NO_FILL;
                    break;
            }

            return adMobErrorCode;
        }
    }

    static class ServerExtrasParser {

        private static final String BLOCK_ID = "blockID";
        private static final String OPEN_LINKS_IN_APP = "openLinksInApp";

        @Nullable
        String parseBlockId(@Nullable final Bundle serverParameters) {
            final JSONObject serverExtras = convertToServerExtras(serverParameters);
            return serverExtras != null ? serverExtras.optString(BLOCK_ID) : null;
        }

        boolean parseShouldOpenLinksInApp(@Nullable final Bundle serverParameters) {
            final JSONObject serverExtras = convertToServerExtras(serverParameters);
            return serverExtras != null && serverExtras.optBoolean(OPEN_LINKS_IN_APP);
        }

        @Nullable
        private JSONObject convertToServerExtras(@Nullable final Bundle serverParameters) {
            if (serverParameters != null) {
                final String parameters = serverParameters
                        .getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
                try {
                    return new JSONObject(parameters);
                } catch (final JSONException exception) {
                    Log.w(TAG, "Can't parse server parameters");
                }
            }

            return null;
        }
    }
}
