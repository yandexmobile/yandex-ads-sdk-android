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
import com.yandex.mobile.ads.video.models.ad.VideoAd;

import java.util.List;

public class VideoAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface TrackingEventListener {
        void trackEvents(@NonNull final VideoAd videoAd);
    }

    @NonNull
    private final TrackingEventListener mTrackingEventListener;

    @NonNull
    private final DescriptionProvider mDescriptionProvider;

    @Nullable
    private List<VideoAd> mData;

    public VideoAdAdapter(@NonNull final TrackingEventListener trackingEventListener) {
        mTrackingEventListener = trackingEventListener;
        mDescriptionProvider = new DescriptionProvider();
    }

    public void setData(@NonNull final List<VideoAd> videoAds) {
        mData = videoAds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                      final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_videoad, parent, false);
        return new Holder.VideoAdHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,
                                 final int position) {
        bindVideoAd((Holder.VideoAdHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    private void bindVideoAd(@NonNull final Holder.VideoAdHolder viewHolder,
                             final int position) {
        if (mData != null) {
            final List<VideoAd> videoAds = mData;
            if (position < videoAds.size()) {
                final VideoAd videoAd = videoAds.get(position);
                final String description = mDescriptionProvider.getVideoAdDescription(videoAd);
                viewHolder.text.setText(description);

                viewHolder.tracking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mTrackingEventListener.trackEvents(videoAd);
                    }
                });
            }
        }
    }
}
