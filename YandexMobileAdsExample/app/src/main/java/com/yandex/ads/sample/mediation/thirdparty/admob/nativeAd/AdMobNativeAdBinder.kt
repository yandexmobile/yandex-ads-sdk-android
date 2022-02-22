/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.mediation.thirdparty.admob.nativeAd

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.yandex.ads.sample.databinding.NativeAdAdmobViewBinding

class AdMobNativeAdBinder {

    fun bindNativeAd(
        binding: NativeAdAdmobViewBinding,
        nativeAd: NativeAd,
    ) {
        val nativeAdView = binding.nativeAdView
        binding.components.isVisible = true

        setupComponentWithText(binding.sponsored, nativeAd.advertiser, nativeAdView::setAdvertiserView)
        setupComponentWithText(binding.body, nativeAd.body, nativeAdView::setBodyView)
        setupComponentWithText(binding.callToAction, nativeAd.callToAction, nativeAdView::setCallToActionView)
        setupComponentWithText(binding.title, nativeAd.headline, nativeAdView::setHeadlineView)
        setupComponentWithText(binding.price, nativeAd.price, nativeAdView::setPriceView)
        setupComponentWithText(binding.domain, nativeAd.store, nativeAdView::setStoreView)
        setupIcon(binding, nativeAd, nativeAdView)
        setupRating(nativeAd, binding)
        nativeAdView.starRatingView = binding.rating
        nativeAdView.mediaView = binding.media

        nativeAdView.setNativeAd(nativeAd)
    }

    private fun setupRating(
        nativeAd: NativeAd,
        binding: NativeAdAdmobViewBinding
    ) {
        val starRating = nativeAd.starRating
        binding.rating.isVisible = false
        if (starRating != null) {
            binding.rating.rating = starRating.toFloat()
            binding.rating.isVisible = true
        }
    }

    private fun setupIcon(
        binding: NativeAdAdmobViewBinding,
        nativeAd: NativeAd,
        nativeAdView: NativeAdView
    ) {
        binding.icon.apply {
            setImageDrawable(nativeAd.icon?.drawable)
        }
        nativeAdView.iconView = binding.icon
    }

    private fun setupComponentWithText(
        component: TextView,
        text: String?,
        addToRoot: (View) -> Unit,
    ) {
        component.text = text
        addToRoot(component)
    }

    fun clearNativeAdView(binding: NativeAdAdmobViewBinding) {
        binding.components.isVisible = false
    }
}