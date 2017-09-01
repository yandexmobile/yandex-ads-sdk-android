/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.nativeads.template.recyclerview.sample;

import android.support.annotation.NonNull;
import android.util.Pair;
import android.util.SparseArray;

import java.util.List;

public class NativeBlockContentHelper {

    private SparseArray<Integer> mPositionViewType;

    public NativeBlockContentHelper() {
        mPositionViewType = new SparseArray<>();
    }

    public void setData(@NonNull List<Pair<Integer, Object>> dataList) {
        mPositionViewType.clear();

        for (int i = 0; i < dataList.size(); i++) {
            final Pair<Integer, Object> pair = dataList.get(i);
            mPositionViewType.put(i, pair.first);
        }
    }

    public int getItemType(final int position) {
        Integer type = mPositionViewType.get(position);
        return type != null ? type : Holder.BlockContentProvider.NONE_TYPE;
    }

    public int getItemCount() {
        return mPositionViewType.size();
    }
}
