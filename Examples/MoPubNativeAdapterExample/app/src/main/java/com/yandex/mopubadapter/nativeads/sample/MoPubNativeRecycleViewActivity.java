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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubServerPositioning;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;
import com.mopub.nativeads.YandexNativeAdRenderer;
import com.mopub.nativeads.YandexViewBinder;
import com.yandex.mopubadapter.nativeads.sample.MoPubInitializer.MoPubInitializerListener;
import com.yandex.mopubadapter.nativeads.sample.adapter.SimpleDataAdapter;


public class MoPubNativeRecycleViewActivity extends AppCompatActivity {

    /*
     * Replace AD_UNIT_ID with your unique Ad Unit ID.
     * Please, read official documentation how to obtain one: {@link https://app.mopub.com}
     */
    private static final String AD_UNIT_ID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private MoPubRecyclerAdapter mMoPubRecyclerAdapter;
    private MoPubInitializer mMoPubInitializer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler_view);

        final Button loadNativeAdButton = findViewById(R.id.load_ad_button);
        loadNativeAdButton.setOnClickListener(mMoPubNativeAdClickListener);

        final RecyclerView recyclerView = findViewById(R.id.native_ads_list);
        mMoPubRecyclerAdapter = createMoPubRecyclerAdapter();
        initializeRecycleView(recyclerView, mMoPubRecyclerAdapter);
        mMoPubInitializer = new MoPubInitializer();
    }

    private void initializeRecycleView(@NonNull final RecyclerView recyclerView,
                                       @NonNull final MoPubRecyclerAdapter moPubRecyclerAdapter) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(moPubRecyclerAdapter);
    }

    private final View.OnClickListener mMoPubNativeAdClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            MoPubNativeRecycleViewActivity.this.refreshMoPubNativeAd();
        }
    };

    private void refreshMoPubNativeAd() {
        mMoPubRecyclerAdapter.clearAds();

        final RequestParameters requestParameters = new RequestParameters.Builder().build();
        mMoPubInitializer.initializeSdk(this, AD_UNIT_ID, new MoPubInitializerListener() {
            @Override
            public void onMoPubSdkInitialized() {
                mMoPubRecyclerAdapter.loadAds(AD_UNIT_ID, requestParameters);
            }
        });
    }

    @NonNull
    private MoPubRecyclerAdapter createMoPubRecyclerAdapter() {
        final RecyclerView.Adapter simpleDataAdapter = new SimpleDataAdapter();
        final MoPubServerPositioning serverPositioning = new MoPubServerPositioning();
        final MoPubRecyclerAdapter moPubRecyclerAdapter =
                new MoPubRecyclerAdapter(this, simpleDataAdapter, serverPositioning);
        registerNativeAdRenderers(moPubRecyclerAdapter);

        return moPubRecyclerAdapter;
    }

    private void registerNativeAdRenderers(@NonNull final MoPubRecyclerAdapter moPubRecyclerAdapter) {
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

        moPubRecyclerAdapter.registerAdRenderer(new MoPubStaticNativeAdRenderer(moPubViewBinder));
        moPubRecyclerAdapter.registerAdRenderer(new YandexNativeAdRenderer(yandexViewBinder));
    }

    @Override
    protected void onDestroy() {
        mMoPubRecyclerAdapter.destroy();
        super.onDestroy();
    }
}
