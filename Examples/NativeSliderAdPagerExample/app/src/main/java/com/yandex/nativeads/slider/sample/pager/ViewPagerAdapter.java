/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.slider.sample.pager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder;
import com.yandex.nativeads.slider.sample.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<NativeAdViewBinderHolder> {

    private static final String VIEW_PAGER_TAG = "ViewPagerAdapter";

    @NonNull
    private final LayoutInflater mLayoutInflater;

    @Nullable
    private List<NativeAd> mNativeAds;

    public ViewPagerAdapter(@NonNull final Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(@NonNull final List<NativeAd> nativeAds) {
        mNativeAds = nativeAds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NativeAdViewBinderHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                       final int viewType) {
        final View rootView = mLayoutInflater.inflate(R.layout.widget_native_ad, parent, false);
        return new NativeAdViewBinderHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NativeAdViewBinderHolder holder,
                                 final int position) {
        if (mNativeAds != null) {
            final NativeAd nativeAd = mNativeAds.get(position);
            bindNativeAd(nativeAd, holder.getNativeAdViewBinder());
        }
    }

    @Override
    public int getItemCount() {
        return mNativeAds != null ? mNativeAds.size() : 0;
    }

    private void bindNativeAd(@NonNull final NativeAd nativeAd,
                              @NonNull final NativeAdViewBinder viewBinder) {
        try {
            nativeAd.bindNativeAd(viewBinder);
        } catch (final NativeAdException exception) {
            Log.d(VIEW_PAGER_TAG, exception.getMessage());
        }
    }
}
