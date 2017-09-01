/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.nativeads.template.recyclerview.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeContentAd;

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

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        mAdapter = new NativeTemplateAdapter();
        recyclerView.setAdapter(mAdapter);

        createNativeAdLoader();
    }

    private void createNativeAdLoader() {
        /*
        * Replace demo R-M-DEMO-native-i with actual Block ID
        * Please, note, that configured image sizes don't affect demo ads.
        * Following demo Block IDs may be used for testing:
        * app install: R-M-DEMO-native-i
        * content: R-M-DEMO-native-c
        */
        final NativeAdLoaderConfiguration adLoaderConfiguration =
                new NativeAdLoaderConfiguration.Builder("R-M-DEMO-native-c", false)
                        .setImageSizes(NativeAdLoaderConfiguration.NATIVE_IMAGE_SIZE_LARGE).build();
        mNativeAdLoader = new NativeAdLoader(getApplicationContext(), adLoaderConfiguration);
        mNativeAdLoader.setOnLoadListener(mNativeAdLoadListener);
    }

    private void loadAd() {
        mNativeAdLoader.loadAd(AdRequest.builder().build());
    }

    private NativeAdLoader.OnLoadListener mNativeAdLoadListener = new NativeAdLoader.OnLoadListener(){
        @Override
        public void onAppInstallAdLoaded(@NonNull final NativeAppInstallAd nativeAppInstallAd) {
            fillData(new Pair<Integer, Object>(Holder.BlockContentProvider.NATIVE_BANNER, nativeAppInstallAd));
        }

        @Override
        public void onContentAdLoaded(@NonNull final NativeContentAd nativeContentAd) {
            fillData(new Pair<Integer, Object>(Holder.BlockContentProvider.NATIVE_BANNER, nativeContentAd));
        }

        @Override
        public void onAdFailedToLoad(@NonNull final AdRequestError error) {
            Log.d(SAMPLE_TAG, error.getDescription());
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

    private View.OnClickListener mLoadButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            loadAd();
        }
    };
}
