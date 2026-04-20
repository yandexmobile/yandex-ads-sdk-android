package com.yandex.ads.sample.nativeads

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.yandex.ads.sample.databinding.NativeBannerAdViewBinding
import com.yandex.mobile.ads.common.AdBindingResult
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder

class NativeAdBannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = NativeBannerAdViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun bindNativeAd(nativeAd: NativeAd) {
        val binder = binding.run {
            NativeAdViewBinder.Builder(nativeAdContainer)
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
        isVisible = nativeAd.bindNativeAd(binder) is AdBindingResult.Success
    }

    fun release() {
        isVisible = false
    }
}
