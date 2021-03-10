/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.videoad.sample.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.ads.videoad.sample.R;

public class Holder {

    public static class VmapViewType {
        public static final int VERSION = 0;
        public static final int EXTENSIONS = 1;
        public static final int AD_BREAK = 2;
    }

    public static class ExtensionsHolder extends RecyclerView.ViewHolder {

        public final TextView text;

        public ExtensionsHolder(@NonNull final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public static class AdBreakHolder extends RecyclerView.ViewHolder {

        public final TextView text;
        public final Button loadVast;

        public AdBreakHolder(@NonNull final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            loadVast = itemView.findViewById(R.id.load_vast);
        }
    }

    public static class VersionHolder extends RecyclerView.ViewHolder {

        public final TextView text;

        VersionHolder(final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public static class VideoAdHolder extends RecyclerView.ViewHolder {

        public final TextView text;
        public final Button tracking;

        public VideoAdHolder(@NonNull final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            tracking = itemView.findViewById(R.id.tracking);
        }
    }
}
