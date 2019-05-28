/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mopubadapter.banner.sample;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;

class MoPubInitializer {

    public interface MoPubInitializerListener {
        void onMoPubSdkInitialized();
    }

    void initializeSdk(@NonNull final Context context,
                       @NonNull final String adUnitId,
                       @NonNull final MoPubInitializerListener moPubInitializerListener) {
        if (MoPub.isSdkInitialized()) {
            moPubInitializerListener.onMoPubSdkInitialized();
        } else {
            final SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adUnitId).build();
            final SdkInitializationListener initializationListener = moPubInitializerListener::onMoPubSdkInitialized;
            MoPub.initializeSdk(context, sdkConfiguration, initializationListener);
        }
    }
}
