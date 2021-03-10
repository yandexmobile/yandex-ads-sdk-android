/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2020 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.nativeads.slider.sample.pager.transformer;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class SliderTransformer implements ViewPager2.PageTransformer {

    private static final float FULL_VISIBILITY_SCALE = 1.f;
    private static final float NEXT_ITEM_SCALE_FACTOR = 0.85f;

    @Override
    public void transformPage(@NonNull final View page,
                              final float position) {
        final float scale = computeScale(position);
        page.setScaleY(scale);
        page.setScaleX(scale);

        final float offset = computeOffset(page, position, scale);
        page.setTranslationX(offset);

        final float inversedDepth = -position;
        page.setTranslationZ(inversedDepth);
    }

    private float computeScale(final float position) {
        final float scale;
        if (position >= 0) {
            final float scalePosition = 1 - Math.abs(position);
            scale = NEXT_ITEM_SCALE_FACTOR + scalePosition * (FULL_VISIBILITY_SCALE - NEXT_ITEM_SCALE_FACTOR);
        } else {
            scale = FULL_VISIBILITY_SCALE;
        }

        return scale;
    }

    private float computeOffset(@NonNull final View page,
                                final float position,
                                final float scale) {
        final float offset;
        if (position >= 0) {
            final int viewWidth = page.getMeasuredWidth();
            final MarginLayoutParams marginLayoutParams = (MarginLayoutParams) page.getLayoutParams();
            final int pageRightMarginPx = marginLayoutParams.rightMargin;
            final int pagePaddingPx = page.getPaddingRight();
            final int viewFullWidth = page.getMeasuredWidth() + pageRightMarginPx;

            final float pageCenteredOffset = position * viewFullWidth;
            final float contentEdgeOffset = (viewWidth * scale - pageRightMarginPx) / 2.f - pagePaddingPx * scale;
            final float containerEdgeOffset = (viewWidth - pageRightMarginPx) / 2.f - pagePaddingPx;
            final float marginOffset = position * (pageRightMarginPx + pagePaddingPx);

            offset = -pageCenteredOffset - contentEdgeOffset + containerEdgeOffset + marginOffset;
        } else {
            offset = page.getTranslationX();
        }

        return offset;
    }
}
