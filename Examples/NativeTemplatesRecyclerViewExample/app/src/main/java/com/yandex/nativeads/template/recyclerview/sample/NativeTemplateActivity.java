/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.nativeads.template.recyclerview.sample;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;

import java.util.ArrayList;
import java.util.List;

public class NativeTemplateActivity extends AppCompatActivity {

    private static final String SAMPLE_TAG = "NativeTemplatesExample";

    private NativeAdLoader mNativeAdLoader;
    private NativeTemplateAdapter mAdapter;

    private final List<Pair<Integer, Object>> mData = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_template);

        final Button loadButton = (Button) findViewById(R.id.load_native_template_ad);
        loadButton.setOnClickListener(mLoadButtonClickListener);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.native_template_recycler_view);
        recyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        mAdapter = new NativeTemplateAdapter();
        recyclerView.setAdapter(mAdapter);

        createNativeAdLoader();
    }

    private void createNativeAdLoader() {
        mNativeAdLoader = new NativeAdLoader(this);
        mNativeAdLoader.setNativeAdLoadListener(mNativeAdLoadListener);
    }

    private void loadAd() {
        /*
         * Replace demo R-M-DEMO-native-i with actual Block ID
         * Please, note, that configured image sizes don't affect demo ads.
         * Following demo Block IDs may be used for testing:
         * app install: R-M-DEMO-native-i
         * content: R-M-DEMO-native-c
         */
        final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                new NativeAdRequestConfiguration.Builder("R-M-DEMO-native-c")
                        .setShouldLoadImagesAutomatically(false)
                        .build();
        mNativeAdLoader.loadAd(nativeAdRequestConfiguration);
    }

    private final NativeAdLoadListener mNativeAdLoadListener = new NativeAdLoadListener(){
        @Override
        public void onAdLoaded(@NonNull final NativeAd nativeAd) {
            fillData(new Pair<Integer, Object>(Holder.BlockContentProvider.NATIVE_BANNER, nativeAd));
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
            Log.d(SAMPLE_TAG, adRequestError.getDescription());
        }

        private void fillData(@NonNull final Pair<Integer, Object> nativeAd) {
            mData.add(nativeAd);
            int size = mData.size();
            for (int i = size; i < size + 10; i++) {
                final Pair<Integer, Object> pair = new Pair<Integer, Object>(Holder.BlockContentProvider.DEFAULT, i + 1);
                mData.add(pair);
            }
            mAdapter.setData(mData);
        }
    };

    private final View.OnClickListener mLoadButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            loadAd();
        }
    };
}
