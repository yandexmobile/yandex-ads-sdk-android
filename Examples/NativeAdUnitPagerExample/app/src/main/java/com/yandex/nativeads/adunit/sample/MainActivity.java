/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.adunit.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.PageTransformer;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdUnit;
import com.yandex.mobile.ads.nativeads.NativeAdUnitLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdUnitLoader;
import com.yandex.mobile.ads.nativeads.NativeAdUnitView;
import com.yandex.nativeads.adunit.sample.ads.NativeAdUnitLoaderProvider;
import com.yandex.nativeads.adunit.sample.ads.NativeAdsBinder;
import com.yandex.nativeads.adunit.sample.pager.ViewPagerAdapter;
import com.yandex.nativeads.adunit.sample.pager.transformer.CarouselTransformer;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private ViewPagerAdapter mViewPagerAdapter;
    private NativeAdUnitView mNativeAdUnitView;

    private NativeAdsBinder mNativeAdsBinder;
    private NativeAdUnitLoader mNativeAdUnitLoader;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_unit);

        mNativeAdsBinder = new NativeAdsBinder();

        final NativeAdUnitLoaderProvider adUnitLoaderProvider = new NativeAdUnitLoaderProvider();
        mNativeAdUnitLoader = adUnitLoaderProvider.createNativeAdUnitLoader(this);
        mNativeAdUnitLoader.setNativeAdUnitLoadListener(new NativeAdLoadListener());

        mNativeAdUnitView = findViewById(R.id.native_ad_unit_container);

        final ViewPager2 viewPager = findViewById(R.id.native_ads_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(mViewPagerAdapter);

        final PageTransformer pageTransformer = new CarouselTransformer();
        viewPager.setPageTransformer(pageTransformer);
        viewPager.setOffscreenPageLimit(3);
    }

    public void onClickLoadAdButton(@NonNull final View view) {
        mNativeAdUnitView.setVisibility(View.GONE);

        final AdRequest adRequest = AdRequest.builder().build();
        mNativeAdUnitLoader.loadAdUnit(adRequest);
    }

    private class NativeAdLoadListener implements NativeAdUnitLoadListener {

        @Override
        public void onNativeAdUnitLoaded(@NonNull final NativeAdUnit nativeAdUnit) {
            mNativeAdsBinder.bindAd(nativeAdUnit, mNativeAdUnitView, mViewPagerAdapter);
            mNativeAdUnitView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNativeAdUnitFailedToLoad(@NonNull final AdRequestError adRequestError) {
            Log.d(TAG, adRequestError.getDescription());
            mNativeAdUnitView.setVisibility(View.GONE);
        }
    }
}