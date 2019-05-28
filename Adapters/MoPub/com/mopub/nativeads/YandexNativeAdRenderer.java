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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mopub.common.logging.MoPubLog;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder;
import com.yandex.mobile.ads.nativeads.NativeGenericAd;

import java.util.WeakHashMap;

public class YandexNativeAdRenderer implements MoPubAdRenderer<YandexNativeAd> {

    @NonNull
    private final ViewBinder mViewBinder;

    @NonNull
    private final WeakHashMap<View, YandexNativeViewHolder> mViewHolderMap;

    public YandexNativeAdRenderer(@NonNull final ViewBinder viewBinder) {
        mViewBinder = viewBinder;
        mViewHolderMap = new WeakHashMap<>();
    }

    @NonNull
    @Override
    public View createAdView(@NonNull final Context context, @Nullable final ViewGroup parent) {
        return LayoutInflater.from(context).inflate(mViewBinder.layoutId, parent, false);
    }

    @Override
    public void renderAdView(@NonNull final View view,
                             @NonNull final YandexNativeAd yandexNativeAd) {
        YandexNativeViewHolder nativeViewHolder = mViewHolderMap.get(view);
        if (nativeViewHolder == null) {
            nativeViewHolder = new YandexNativeViewHolder(view, mViewBinder);
            mViewHolderMap.put(view, nativeViewHolder);
        }

        render(view, yandexNativeAd, nativeViewHolder);
        view.setVisibility(View.VISIBLE);
    }

    private void render(@NonNull final View view,
                        @NonNull final YandexNativeAd yandexNativeAd,
                        @NonNull final YandexNativeViewHolder nativeViewHolder) {
        final NativeGenericAd yandexAd = yandexNativeAd.getNativeGeneticAd();
        
        final NativeAdViewBinder viewBinder = new NativeAdViewBinder.Builder(view)
            .setAgeView(nativeViewHolder.getAgeView())
            .setBodyView(nativeViewHolder.getBodyView())
            .setCallToActionView(nativeViewHolder.getCallToActionView())
            .setDomainView(nativeViewHolder.getDomainView())
            .setFaviconView(nativeViewHolder.getFaviconView())
            .setIconView(nativeViewHolder.getIconView())
            .setImageView(nativeViewHolder.getImageView())
            .setPriceView(nativeViewHolder.getPriceView())
            .setRatingView(nativeViewHolder.getRatingView())
            .setReviewCountView(nativeViewHolder.getReviewCountView())
            .setSponsoredView(nativeViewHolder.getSponsoredView())
            .setTitleView(nativeViewHolder.getTitleView())
            .setWarningView(nativeViewHolder.getWarningView()).build();

        try {
            yandexAd.bindNativeAd(viewBinder);
        } catch (final NativeAdException exception) {
            MoPubLog.e(exception.toString(), exception);
        }
    }

    @Override
    public boolean supports(@NonNull final BaseNativeAd nativeAd) {
        return nativeAd instanceof YandexNativeAd;
    }
}
