/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.adunit.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRatingBar;

import com.yandex.mobile.ads.nativeads.Rating;

public class MyRatingView extends AppCompatRatingBar implements Rating {

    public MyRatingView(@NonNull final Context context) {
        super(context);
    }

    public MyRatingView(@NonNull final Context context,
                        @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRatingView(@NonNull final Context context,
                        @Nullable final AttributeSet attrs,
                        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setRating(@NonNull final Float rating) {
        super.setRating(rating);
    }
}