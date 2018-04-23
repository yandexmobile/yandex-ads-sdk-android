/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.video.sample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yandex.ads.video.sample.R;
import com.yandex.ads.video.sample.model.TrackingModel;

import java.util.List;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> {

    public interface OnTrackingItemClickListener {
        void onTrackingItemClick(@NonNull final TrackingModel model);
    }

    @NonNull
    private final List<TrackingModel> mTrackingItems;

    @NonNull
    private final OnTrackingItemClickListener mOnTrackingItemClickListener;

    public TrackingAdapter(@NonNull final List<TrackingModel> trackingItems,
                           @NonNull final OnTrackingItemClickListener listener) {
        mTrackingItems = trackingItems;
        mOnTrackingItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TrackingModel model = mTrackingItems.get(position);
        fillViewHolder(holder, model, mOnTrackingItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mTrackingItems.size();
    }

    private void fillViewHolder(@NonNull final ViewHolder holder,
                                @NonNull final TrackingModel trackingModel,
                                @NonNull final OnTrackingItemClickListener listener) {
        holder.mTrackingButton.setText(trackingModel.getEventName());
        holder.mTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listener.onTrackingItemClick(trackingModel);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button mTrackingButton;

        ViewHolder(final View itemView) {
            super(itemView);

            mTrackingButton = itemView.findViewById(R.id.id_tracking_button);
        }
    }
}