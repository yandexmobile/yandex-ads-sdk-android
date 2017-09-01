/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.nativeads.template.recyclerview.sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yandex.mobile.ads.nativeads.template.NativeBannerView;

public class Holder {

    public class BlockContentProvider {
        public static final int NONE_TYPE = -1;
        public static final int DEFAULT = 0;
        public static final int NATIVE_BANNER = 1;
    }

    public static class ListItemHolder extends RecyclerView.ViewHolder {

        public ListItemHolder(View itemView) {
            super(itemView);
        }
    }


    public static class NativeBannerViewHolder extends RecyclerView.ViewHolder {

        public final NativeBannerView nativeBannerView;

        public NativeBannerViewHolder(View itemView) {
            super(itemView);
            nativeBannerView = (NativeBannerView) itemView.findViewById(R.id.native_template);
        }
    }
}
