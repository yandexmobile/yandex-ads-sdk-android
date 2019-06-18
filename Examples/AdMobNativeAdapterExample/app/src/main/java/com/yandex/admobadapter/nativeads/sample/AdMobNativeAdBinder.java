/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.admobadapter.nativeads.sample;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import static android.view.View.GONE;

class AdMobNativeAdBinder {

    void bindNativeAd(final UnifiedNativeAd nativeAd,
                      final UnifiedNativeAdView nativeAdView) {
        final TextView advertiser = nativeAdView.findViewById(R.id.sponsored);
        advertiser.setText(nativeAd.getAdvertiser());
        advertiser.setVisibility(View.VISIBLE);
        nativeAdView.setAdvertiserView(advertiser);

        final TextView body = nativeAdView.findViewById(R.id.body);
        body.setText(nativeAd.getBody());
        body.setVisibility(View.VISIBLE);
        nativeAdView.setBodyView(body);

        final TextView callToAction = nativeAdView.findViewById(R.id.call_to_action);
        callToAction.setText(nativeAd.getCallToAction());
        callToAction.setVisibility(View.VISIBLE);
        nativeAdView.setCallToActionView(callToAction);

        final TextView headline = nativeAdView.findViewById(R.id.title);
        headline.setText(nativeAd.getHeadline());
        headline.setVisibility(View.VISIBLE);
        nativeAdView.setHeadlineView(headline);

        final ImageView icon = nativeAdView.findViewById(R.id.icon);
        icon.setImageDrawable(nativeAd.getIcon() != null ? nativeAd.getIcon().getDrawable() : null);
        icon.setVisibility(View.VISIBLE);
        nativeAdView.setIconView(icon);

        final TextView price = nativeAdView.findViewById(R.id.price);
        price.setText(nativeAd.getPrice());
        price.setVisibility(View.VISIBLE);
        nativeAdView.setPriceView(price);

        final RatingBar rating = nativeAdView.findViewById(R.id.rating);
        final Double starRating = nativeAd.getStarRating();
        if (starRating != null) {
            rating.setRating(starRating.floatValue());
            rating.setVisibility(View.VISIBLE);
            nativeAdView.setStarRatingView(rating);
        }

        final TextView store = nativeAdView.findViewById(R.id.domain);
        store.setText(nativeAd.getStore());
        store.setVisibility(View.VISIBLE);
        nativeAdView.setStoreView(store);

        final MediaView mediaView = nativeAdView.findViewById(R.id.media);
        mediaView.setVisibility(View.VISIBLE);
        nativeAdView.setMediaView(mediaView);

        nativeAdView.setNativeAd(nativeAd);
    }

    void clearNativeAdView(final UnifiedNativeAdView nativeAdView) {
        nativeAdView.findViewById(R.id.age).setVisibility(GONE);
        nativeAdView.findViewById(R.id.sponsored).setVisibility(GONE);
        nativeAdView.findViewById(R.id.body).setVisibility(GONE);
        nativeAdView.findViewById(R.id.call_to_action).setVisibility(GONE);
        nativeAdView.findViewById(R.id.title).setVisibility(GONE);
        nativeAdView.findViewById(R.id.icon).setVisibility(GONE);
        nativeAdView.findViewById(R.id.price).setVisibility(GONE);
        nativeAdView.findViewById(R.id.rating).setVisibility(GONE);
        nativeAdView.findViewById(R.id.domain).setVisibility(GONE);
        nativeAdView.findViewById(R.id.media).setVisibility(GONE);

        nativeAdView.findViewById(R.id.favicon).setVisibility(GONE);
        nativeAdView.findViewById(R.id.feedback).setVisibility(GONE);
        nativeAdView.findViewById(R.id.review_count).setVisibility(GONE);
        nativeAdView.findViewById(R.id.warning).setVisibility(GONE);
    }
}
