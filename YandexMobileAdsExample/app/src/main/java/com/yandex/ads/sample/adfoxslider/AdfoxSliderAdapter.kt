/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.adfoxslider

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yandex.ads.sample.databinding.AdfoxSliderItemBinding
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdException
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder

class AdfoxSliderAdapter : RecyclerView.Adapter<NativeAdViewBinderHolder>() {

    private var nativeAds: List<NativeAd> = listOf()

    fun setData(nativeAds: List<NativeAd>) {
        this.nativeAds = nativeAds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NativeAdViewBinderHolder {
        val binding = AdfoxSliderItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NativeAdViewBinderHolder(binding)
    }

    override fun onBindViewHolder(holder: NativeAdViewBinderHolder, position: Int) {
        val nativeAd = nativeAds[position]
        bindNativeAd(nativeAd, holder.nativeAdViewBinder)
    }

    override fun getItemCount(): Int {
        return nativeAds.size
    }

    private fun bindNativeAd(nativeAd: NativeAd, viewBinder: NativeAdViewBinder) {
        try {
            nativeAd.bindNativeAd(viewBinder)
        } catch (exception: NativeAdException) {
            Log.d(VIEW_PAGER_TAG, "${exception.message}")
        }
    }

    companion object {

        private const val VIEW_PAGER_TAG = "ViewPagerAdapter"
    }
}
