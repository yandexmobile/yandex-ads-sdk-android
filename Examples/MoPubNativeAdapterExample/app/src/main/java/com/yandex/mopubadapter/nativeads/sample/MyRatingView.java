/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2016 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.mopubadapter.nativeads.sample;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRatingBar;

import com.yandex.mobile.ads.nativeads.Rating;

public class MyRatingView extends AppCompatRatingBar implements Rating {

    public MyRatingView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyRatingView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRatingView(final Context context) {
        super(context);
    }

    @Override
    public void setRating(final float rating) {
        super.setRating(rating);
    }

    @Override
    public float getRating() {
        return super.getRating();
    }
}