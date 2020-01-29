/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.mopubadapter.nativeads.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;
import com.mopub.nativeads.YandexNativeAdRenderer;
import com.mopub.nativeads.YandexViewBinder;
import com.yandex.mopubadapter.nativeads.sample.MoPubInitializer.MoPubInitializerListener;

public class MoPubNativeBannerActivity extends AppCompatActivity {

    private static final int MOPUB_ADAPTER_SHOW_AD_START_POSITION = 0;
    private static final int MOPUB_ADAPTER_SHOW_AD_INTERVAL = 2;

    /*
     * Replace AD_UNIT_ID with your unique Ad Unit ID.
     * Please, read official documentation how to obtain one: {@link https://app.mopub.com}
     */
    private static final String AD_UNIT_ID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private RequestParameters mRequestParameters;
    private MoPubNative mMoPubNative;
    private MoPubInitializer mMoPubInitializer;
    private AdapterHelper mAdapterHelper;

    private ViewGroup mNativeAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner);

        mNativeAdContainer = findViewById(R.id.native_ad_container);

        final Button loadNativeAdButton = findViewById(R.id.load_ad_button);
        loadNativeAdButton.setOnClickListener(mMoPubNativeAdClickListener);

        initMoPubNativeAd();
    }

    private void initMoPubNativeAd() {
        mMoPubInitializer = new MoPubInitializer();
        mAdapterHelper = new AdapterHelper(this, MOPUB_ADAPTER_SHOW_AD_START_POSITION, MOPUB_ADAPTER_SHOW_AD_INTERVAL);
        mRequestParameters = new RequestParameters.Builder().build();

        final MoPubNativeListener moPubNativeListener = new MoPubNativeListener();
        mMoPubNative = new MoPubNative(this, AD_UNIT_ID, moPubNativeListener);
        registerNativeAdRenderers(mMoPubNative);
    }

    private void registerNativeAdRenderers(@NonNull final MoPubNative moPubNative) {
        final ViewBinder moPubViewBinder = new ViewBinder.Builder(R.layout.widget_native_ad_mopub_image)
                .callToActionId(R.id.call_to_action)
                .iconImageId(R.id.icon)
                .mainImageId(R.id.image)
                .textId(R.id.body)
                .titleId(R.id.title)
                .privacyInformationIconImageId(R.id.privacy_information_icon)
                .build();

        final YandexViewBinder yandexViewBinder = new YandexViewBinder.Builder(R.layout.widget_native_ad_yandex)
                .setAgeId(R.id.age)
                .setBodyId(R.id.body)
                .setCallToActionId(R.id.call_to_action)
                .setDomainId(R.id.domain)
                .setFaviconId(R.id.favicon)
                .setIconId(R.id.icon)
                .setMediaId(R.id.media)
                .setPriceId(R.id.price)
                .setRatingId(R.id.rating)
                .setReviewCountId(R.id.review_count)
                .setSponsoredId(R.id.sponsored)
                .setTitleId(R.id.title)
                .setWarningId(R.id.warning)
                .build();

        moPubNative.registerAdRenderer(new MoPubStaticNativeAdRenderer(moPubViewBinder));
        moPubNative.registerAdRenderer(new YandexNativeAdRenderer(yandexViewBinder));
    }

    private View.OnClickListener mMoPubNativeAdClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            MoPubNativeBannerActivity.this.refreshMoPubNativeAd();
        }
    };

    private void refreshMoPubNativeAd() {
        mNativeAdContainer.setVisibility(View.GONE);

        mMoPubInitializer.initializeSdk(this, AD_UNIT_ID, new MoPubInitializerListener() {
            @Override
            public void onMoPubSdkInitialized() {
                mMoPubNative.makeRequest(mRequestParameters);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mMoPubNative.destroy();
        super.onDestroy();
    }

    private void renderNativeAdView(@NonNull final NativeAd nativeAd) {
        final View nativeAdView = mAdapterHelper.getAdView(null, mNativeAdContainer, nativeAd);
        final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mNativeAdContainer.removeAllViews();
        mNativeAdContainer.addView(nativeAdView, layoutParams);
        mNativeAdContainer.setVisibility(View.VISIBLE);
    }

    private final class MoPubNativeListener implements MoPubNative.MoPubNativeNetworkListener {

        @Override
        public void onNativeLoad(NativeAd nativeAd) {
            showToast("Ad loaded successfully");
            renderNativeAdView(nativeAd);

            nativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
                @Override
                public void onImpression(final View view) {
                    showToast("onImpression");
                }

                @Override
                public void onClick(final View view) {
                    showToast("onClick");
                }
            });
        }

        @Override
        public void onNativeFail(NativeErrorCode errorCode) {
            showToast("Ad failed to load: " + errorCode.toString());
        }

        private void showToast(@NonNull final String message) {
            Toast.makeText(MoPubNativeBannerActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
