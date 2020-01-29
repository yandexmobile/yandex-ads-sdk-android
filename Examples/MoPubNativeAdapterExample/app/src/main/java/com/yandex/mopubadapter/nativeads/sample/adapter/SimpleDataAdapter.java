/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.mopubadapter.nativeads.sample.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class SimpleDataAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private static final int ITEM_COUNT = 150;

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                               final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder holder,
                                 final int position) {
        final String itemPositionText = String.format(Locale.US, "Content Item #%d", position);
        holder.getTextView().setText(itemPositionText);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
