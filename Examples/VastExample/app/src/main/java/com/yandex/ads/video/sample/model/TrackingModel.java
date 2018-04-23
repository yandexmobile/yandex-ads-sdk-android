/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.video.sample.model;

import android.support.annotation.NonNull;

public class TrackingModel {

    @NonNull
    private final Type mType;

    @NonNull
    private final String mEventName;

    @NonNull
    private final Object mItem;

    public TrackingModel(@NonNull final Type type,
                         @NonNull final String eventName,
                         @NonNull final Object item) {
        mType = type;
        mEventName = eventName;
        mItem = item;
    }

    @NonNull
    public Object getItem() {
        return mItem;
    }

    @NonNull
    public Type getType() {
        return mType;
    }

    @NonNull
    public String getEventName() {
        return mEventName;
    }

    public enum Type {
        AD,
        CREATIVE
    }
}