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
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class SimpleViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    private final TextView mTextView;

    SimpleViewHolder(@NonNull final View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(android.R.id.text1);
    }

    @NonNull
    TextView getTextView() {
        return mTextView;
    }
}
