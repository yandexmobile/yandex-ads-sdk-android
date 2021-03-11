/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.videoad.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.ads.videoad.sample.R;
import com.yandex.mobile.ads.video.models.common.Extension;
import com.yandex.mobile.ads.video.models.vmap.AdBreak;
import com.yandex.mobile.ads.video.models.vmap.Vmap;

import java.util.List;

public class VmapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnAdBreakClickListener {
        void onAdBreakClick(@NonNull final AdBreak adBreak);
    }

    private static final int AD_BREAK_START_POSITION = 2;

    @NonNull
    private final OnAdBreakClickListener mOnAdBreakClickListener;

    @NonNull
    private final DescriptionProvider mDescriptionProvider;

    @Nullable
    private Vmap mData;

    public VmapAdapter(@NonNull final OnAdBreakClickListener onAdBreakClickListener) {
        mOnAdBreakClickListener = onAdBreakClickListener;
        mDescriptionProvider = new DescriptionProvider();
    }

    public void setData(@NonNull final Vmap vmap) {
        mData = vmap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                      final int viewType) {
        switch(viewType) {
            case Holder.VmapViewType.VERSION: {
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_version, parent, false);
                return new Holder.VersionHolder(view);
            }
            case Holder.VmapViewType.EXTENSIONS: {
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_extensions, parent, false);
                return new Holder.ExtensionsHolder(view);
            }
            case Holder.VmapViewType.AD_BREAK:
            default: {
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_adbreak, parent, false);
                return new Holder.AdBreakHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,
                                 final int position) {
        final int viewType = getItemViewType(position);
        switch(viewType) {
            case Holder.VmapViewType.VERSION: {
                bindVersion((Holder.VersionHolder) viewHolder);
                break;
            }
            case Holder.VmapViewType.EXTENSIONS: {
                bindExtensions((Holder.ExtensionsHolder) viewHolder);
                break;
            }
            case Holder.VmapViewType.AD_BREAK: {
                bindAdBreak((Holder.AdBreakHolder) viewHolder, position);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.getAdBreaks().size() + AD_BREAK_START_POSITION : 0;
    }

    @Override
    public int getItemViewType(final int position) {
        final int viewType;
        if (position == 0) {
            viewType = Holder.VmapViewType.VERSION;
        } else if (position == 1) {
            viewType = Holder.VmapViewType.EXTENSIONS;
        } else {
            viewType = Holder.VmapViewType.AD_BREAK;
        }

        return viewType;
    }

    private void bindVersion(@NonNull final Holder.VersionHolder viewHolder) {
        if (mData != null) {
            final String version = mData.getVersion();
            viewHolder.text.setText(String.format("Vmap version = %s", version));
        }
    }

    private void bindExtensions(@NonNull final Holder.ExtensionsHolder viewHolder) {
        if (mData != null) {
            final List<Extension> extensions = mData.getExtensions();
            final String description = mDescriptionProvider.getExtensionsDescription(extensions);

            viewHolder.text.setText(description);
        }
    }

    private void bindAdBreak(@NonNull final Holder.AdBreakHolder viewHolder,
                             final int position) {
        if (mData != null) {
            final int adBreakPosition = position - AD_BREAK_START_POSITION;
            final List<AdBreak> adBreaks = mData.getAdBreaks();
            if (adBreakPosition < adBreaks.size()) {
                final AdBreak adBreak = adBreaks.get(adBreakPosition);
                final String description = mDescriptionProvider.getAdBreakDescription(adBreak);
                viewHolder.text.setText(description);

                viewHolder.loadVast.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mOnAdBreakClickListener.onAdBreakClick(adBreak);
                    }
                });
            }
        }
    }
}
