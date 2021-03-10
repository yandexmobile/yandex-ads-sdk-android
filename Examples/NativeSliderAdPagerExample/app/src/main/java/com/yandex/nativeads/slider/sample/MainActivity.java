/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.slider.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.PageTransformer;

import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.SliderAd;
import com.yandex.mobile.ads.nativeads.SliderAdLoadListener;
import com.yandex.mobile.ads.nativeads.SliderAdLoader;
import com.yandex.mobile.ads.nativeads.SliderAdView;
import com.yandex.nativeads.slider.sample.ads.NativeAdsBinder;
import com.yandex.nativeads.slider.sample.pager.ViewPagerAdapter;
import com.yandex.nativeads.slider.sample.pager.transformer.SliderTransformer;

public class MainActivity extends Activity {

    private static final String BLOCK_ID = "R-M-DEMO-native-ad-unit";
    private static final String TAG = "MainActivity";

    private ViewPagerAdapter mViewPagerAdapter;
    private SliderAdView mSliderAdView;

    private NativeAdsBinder mNativeAdsBinder;
    private SliderAdLoader mSliderAdLoader;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        mNativeAdsBinder = new NativeAdsBinder();

        mSliderAdLoader = new SliderAdLoader(this);
        mSliderAdLoader.setSliderAdLoadListener(new SliderLoadListener());

        mSliderAdView = findViewById(R.id.slider_ad_container);

        final ViewPager2 viewPager = findViewById(R.id.native_ads_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(mViewPagerAdapter);

        final PageTransformer pageTransformer = new SliderTransformer();
        viewPager.setPageTransformer(pageTransformer);
        viewPager.setOffscreenPageLimit(3);
    }

    public void onClickLoadAdButton(@NonNull final View view) {
        mSliderAdView.setVisibility(View.GONE);

        final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                new NativeAdRequestConfiguration.Builder(BLOCK_ID)
                .build();
        mSliderAdLoader.loadSlider(nativeAdRequestConfiguration);
    }

    private class SliderLoadListener implements SliderAdLoadListener {

        @Override
        public void onSliderAdLoaded(@NonNull final SliderAd sliderAd) {
            mNativeAdsBinder.bindAd(sliderAd, mSliderAdView, mViewPagerAdapter);
            mSliderAdView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSliderAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
            Log.d(TAG, adRequestError.getDescription());
            mSliderAdView.setVisibility(View.GONE);
        }
    }
}