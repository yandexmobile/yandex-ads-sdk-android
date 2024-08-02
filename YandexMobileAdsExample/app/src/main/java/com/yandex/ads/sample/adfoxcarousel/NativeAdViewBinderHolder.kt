/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfoxcarousel

import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.databinding.AdfoxCarouselItemBinding
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder

class NativeAdViewBinderHolder(binding: AdfoxCarouselItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val nativeAdViewBinder: NativeAdViewBinder = createNativeAdViewBinder(binding)

    private fun createNativeAdViewBinder(binding: AdfoxCarouselItemBinding): NativeAdViewBinder {
        return binding.nativeAd.run {
            NativeAdViewBinder.Builder(root)
                .setAgeView(age)
                .setBodyView(body)
                .setCallToActionView(callToAction)
                .setDomainView(domain)
                .setFaviconView(favicon)
                .setFeedbackView(feedback)
                .setIconView(icon)
                .setMediaView(media)
                .setPriceView(price)
                .setRatingView(rating)
                .setReviewCountView(reviewCount)
                .setSponsoredView(sponsored)
                .setTitleView(title)
                .setWarningView(warning)
                .build()
        }
    }
}
