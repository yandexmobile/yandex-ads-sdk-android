/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.mopub.nativeads;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdImageLoadingListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeContentAd;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;
import com.yandex.mobile.ads.nativeads.NativeImageAd;

import java.util.HashMap;
import java.util.Map;

public class YandexNative extends CustomEventNative {

    private static final String BLOCK_ID = "blockID";
    private static final String LOCATION = "location";
    private static final String OPEN_LINKS_IN_APP = "openLinksInApp";

    private static final String MEDIATION_NETWORK_KEY = "mediation_network";
    private static final String MEDIATION_NETWORK = "mopub";

    private String mBlockId;
    private Location mLocation;
    private boolean mShouldOpenLinksInApp;

    private CustomEventNativeListener mNativeListener;

    @Override
    protected void loadNativeAd(@NonNull final Context context,
                                @NonNull final CustomEventNativeListener customEventNativeListener,
                                @NonNull final Map<String, Object> localExtras,
                                @NonNull final Map<String, String> serverExtras) {
        mNativeListener = customEventNativeListener;
        if (isValidLocalExtras(localExtras) && isValidServerExtras(serverExtras)) {
            parseLocalExtras(localExtras);
            parseServerExtras(serverExtras);

            final Map<String, String> adRequestParams = new HashMap<>();
            adRequestParams.put(MEDIATION_NETWORK_KEY, MEDIATION_NETWORK);

            final AdRequest adRequest = AdRequest.builder()
                    .withLocation(mLocation)
                    .withParameters(adRequestParams)
                    .build();

            final NativeAdLoaderConfiguration adLoaderConfiguration =
                    new NativeAdLoaderConfiguration.Builder(mBlockId, false).build();

            final NativeAdLoader nativeAdLoader = new NativeAdLoader(context, adLoaderConfiguration);
            nativeAdLoader.setOnLoadListener(new YandexOnLoadListener());
            nativeAdLoader.loadAd(adRequest);
        } else {
            mNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
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
        mShouldOpenLinksInApp = Boolean.parseBoolean(serverExtras.get(OPEN_LINKS_IN_APP));
    }

    private class YandexOnLoadListener implements NativeAdLoader.OnImageAdLoadListener {

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            final NativeErrorCode mopubErrorCode;
            switch (error.getCode()) {
                case AdRequestError.Code.INTERNAL_ERROR:
                case AdRequestError.Code.SYSTEM_ERROR:
                    mopubErrorCode = NativeErrorCode.NETWORK_INVALID_STATE;
                    break;
                case AdRequestError.Code.INVALID_REQUEST:
                    mopubErrorCode = NativeErrorCode.NETWORK_INVALID_REQUEST;
                    break;
                case AdRequestError.Code.NETWORK_ERROR:
                    mopubErrorCode = NativeErrorCode.CONNECTION_ERROR;
                    break;
                case AdRequestError.Code.NO_FILL:
                    mopubErrorCode = NativeErrorCode.NETWORK_NO_FILL;
                    break;
                case AdRequestError.Code.UNKNOWN_ERROR:
                    mopubErrorCode = NativeErrorCode.UNSPECIFIED;
                    break;
                default:
                    mopubErrorCode = NativeErrorCode.NETWORK_NO_FILL;
                    break;
            }

            mNativeListener.onNativeAdFailed(mopubErrorCode);
        }

        @Override
        public void onAppInstallAdLoaded(@NonNull final NativeAppInstallAd nativeAppInstallAd) {
            finishLoading(nativeAppInstallAd);
        }

        @Override
        public void onContentAdLoaded(@NonNull final NativeContentAd nativeContentAd) {
            finishLoading(nativeContentAd);
        }

        @Override
        public void onImageAdLoaded(@NonNull final NativeImageAd nativeImageAd) {
            finishLoading(nativeImageAd);
        }

        private void finishLoading(@NonNull final NativeGenericAd nativeGenericAd) {
            nativeGenericAd.shouldOpenLinksInApp(mShouldOpenLinksInApp);
            nativeGenericAd.addImageLoadingListener(
                    new YandexNativeAdImageLoadingListener(nativeGenericAd));

            nativeGenericAd.loadImages();
        }
    }

    private class YandexNativeAdImageLoadingListener implements NativeAdImageLoadingListener {

        @NonNull
        private final NativeGenericAd mNativeGenericAd;

        YandexNativeAdImageLoadingListener(@NonNull final NativeGenericAd nativeGenericAd) {
            mNativeGenericAd = nativeGenericAd;
        }

        @Override
        public void onFinishLoadingImages() {
            mNativeGenericAd.removeImageLoadingListener(this);
            mNativeListener.onNativeAdLoaded(new YandexNativeAd(mNativeGenericAd));
        }
    }
}
