/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.slider.sample.pager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.nativeads.MediaView;
import com.yandex.mobile.ads.nativeads.NativeAdView;
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder;
import com.yandex.nativeads.slider.sample.MyRatingView;
import com.yandex.nativeads.slider.sample.R;

class NativeAdViewBinderHolder extends RecyclerView.ViewHolder {

    @NonNull
    private final NativeAdViewBinder mNativeAdViewBinder;

    NativeAdViewBinderHolder(@NonNull final View rootView) {
        super(rootView);
        mNativeAdViewBinder = createNativeAdViewBinder(rootView);
    }

    @NonNull
    private NativeAdViewBinder createNativeAdViewBinder(@NonNull final View rootView) {
        final NativeAdView nativeAdView = rootView.findViewById(R.id.native_generic_ad_view);
        return new NativeAdViewBinder.Builder(nativeAdView)
                .setAgeView((TextView) nativeAdView.findViewById(R.id.age))
                .setBodyView((TextView) nativeAdView.findViewById(R.id.body))
                .setCallToActionView((Button) nativeAdView.findViewById(R.id.call_to_action))
                .setDomainView((TextView) nativeAdView.findViewById(R.id.domain))
                .setFaviconView((ImageView) nativeAdView.findViewById(R.id.favicon))
                .setIconView((ImageView) nativeAdView.findViewById(R.id.icon))
                .setMediaView((MediaView) nativeAdView.findViewById(R.id.large_media))
                .setPriceView((TextView) nativeAdView.findViewById(R.id.price))
                .setRatingView((MyRatingView) nativeAdView.findViewById(R.id.rating))
                .setReviewCountView((TextView) nativeAdView.findViewById(R.id.review_count))
                .setSponsoredView((TextView) nativeAdView.findViewById(R.id.sponsored))
                .setTitleView((TextView) nativeAdView.findViewById(R.id.title))
                .setWarningView((TextView) nativeAdView.findViewById(R.id.warning))
                .build();
    }

    @NonNull
    NativeAdViewBinder getNativeAdViewBinder() {
        return mNativeAdViewBinder;
    }
}
