/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2016 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.nativeads.template.recyclerview.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.mobile.ads.nativeads.NativeGenericAd;

import java.util.List;

public class NativeTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final NativeBlockContentHelper mBlockContentHelper;
    private List<Pair<Integer, Object>> mData;

    public NativeTemplateAdapter() {
        mBlockContentHelper = new NativeBlockContentHelper();
    }

    public void setData(@NonNull List<Pair<Integer, Object>> dataList) {
        mData = dataList;
        mBlockContentHelper.setData(mData);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch(viewType) {
            case Holder.BlockContentProvider.DEFAULT: {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false);
                viewHolder = new Holder.ListItemHolder(view);
                break;
            }

            case Holder.BlockContentProvider.NATIVE_BANNER: {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_native_template, parent, false);
                viewHolder = new Holder.NativeBannerViewHolder(view);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch(viewType) {
            case Holder.BlockContentProvider.NATIVE_BANNER: {
                bindNativeBanner((Holder.NativeBannerViewHolder) holder, position);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBlockContentHelper.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mBlockContentHelper.getItemType(position);
    }

    private void bindNativeBanner(final Holder.NativeBannerViewHolder holder, final int position) {
        holder.nativeBannerView.setVisibility(View.GONE);
        final NativeGenericAd nativeAd = (NativeGenericAd) mData.get(position).second;

        holder.nativeBannerView.setAd(nativeAd);
        holder.nativeBannerView.setVisibility(View.VISIBLE);
    }
}
