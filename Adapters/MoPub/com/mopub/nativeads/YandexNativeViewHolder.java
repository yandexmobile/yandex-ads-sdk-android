/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.mopub.nativeads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.common.logging.MoPubLog;
import com.yandex.mobile.ads.nativeads.Rating;

class YandexNativeViewHolder {

    @Nullable
    private final TextView mAgeView;

    @Nullable
    private final TextView mBodyView;

    @Nullable
    private final Button mCallToActionView;

    @Nullable
    private final TextView mDomainView;

    @Nullable
    private final ImageView mFaviconView;

    @Nullable
    private final ImageView mIconView;

    @Nullable
    private final ImageView mImageView;

    @Nullable
    private final TextView mPriceView;

    @Nullable
    private final View mRatingView;

    @Nullable
    private final TextView mReviewCountView;

    @Nullable
    private final TextView mSponsoredView;

    @Nullable
    private final TextView mTitleView;

    @Nullable
    private final TextView mWarningView;

    YandexNativeViewHolder(@NonNull final View view, @NonNull final ViewBinder viewBinder) {
        final ViewFinder<Button> buttonViewFinder = new ButtonViewFinder();
        final ViewFinder<ImageView> imageViewFinder = new ImageViewFinder();
        final ViewFinder<TextView> textViewFinder = new TextViewFinder();

        mAgeView = textViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.AGE));
        mBodyView = textViewFinder.findViewById(view, viewBinder.textId);
        mCallToActionView = buttonViewFinder.findViewById(view, viewBinder.callToActionId);
        mDomainView = textViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.DOMAIN));
        mFaviconView = imageViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.FAVICON));
        mIconView = imageViewFinder.findViewById(view, viewBinder.iconImageId);
        mImageView = imageViewFinder.findViewById(view, viewBinder.mainImageId);
        mPriceView = textViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.PRICE));
        mRatingView = view.findViewById(viewBinder.extras.get(YandexNativeAdAsset.RATING));
        mReviewCountView = textViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.REVIEW_COUNT));
        mSponsoredView = textViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.SPONSORED));
        mTitleView = textViewFinder.findViewById(view, viewBinder.titleId);
        mWarningView = textViewFinder.findViewById(view, viewBinder.extras.get(YandexNativeAdAsset.WARNING));
    }

    @Nullable
    TextView getAgeView() {
        return mAgeView;
    }

    @Nullable
    TextView getBodyView() {
        return mBodyView;
    }

    @Nullable
    Button getCallToActionView() {
        return mCallToActionView;
    }

    @Nullable
    TextView getDomainView() {
        return mDomainView;
    }

    @Nullable
    ImageView getFaviconView() {
        return mFaviconView;
    }

    @Nullable
    ImageView getIconView() {
        return mIconView;
    }

    @Nullable
    ImageView getImageView() {
        return mImageView;
    }

    @Nullable
    TextView getPriceView() {
        return mPriceView;
    }

    @Nullable
    <T extends View & Rating> T getRatingView() {
        final T ratingView;
        if (mRatingView instanceof Rating) {
            ratingView = (T) mRatingView;
        } else {
            MoPubLog.w("Rating view must implements Rating interface. Please, check your rating view.");
            ratingView = null;
        }

        return ratingView;
    }

    @Nullable
    TextView getReviewCountView() {
        return mReviewCountView;
    }

    @Nullable
    TextView getSponsoredView() {
        return mSponsoredView;
    }

    @Nullable
    TextView getTitleView() {
        return mTitleView;
    }

    @Nullable
    TextView getWarningView() {
        return mWarningView;
    }

    private interface ViewFinder<T> {
        String FIND_VIEW_EXCEPTION_MSG = "Could not cast from id in ViewBinder to expected View type";

        T findViewById(@NonNull final View parent, final int childId);
    }

    private static class TextViewFinder implements ViewFinder<TextView> {
        @Override
        public TextView findViewById(@NonNull final View parent, final int childId) {
            try {
                return (TextView) parent.findViewById(childId);
            } catch (final ClassCastException exception) {
                MoPubLog.w(FIND_VIEW_EXCEPTION_MSG, exception);
                return null;
            }
        }
    }

    private class ButtonViewFinder implements ViewFinder<Button> {
        @Override
        public Button findViewById(@NonNull final View parent, final int childId) {
            try {
                return (Button) parent.findViewById(childId);
            } catch (final ClassCastException exception) {
                MoPubLog.w(FIND_VIEW_EXCEPTION_MSG, exception);
                return null;
            }
        }
    }

    private class ImageViewFinder implements ViewFinder<ImageView> {
        @Override
        public ImageView findViewById(@NonNull final View parent, final int childId) {
            try {
                return (ImageView) parent.findViewById(childId);
            } catch (final ClassCastException exception) {
                MoPubLog.w(FIND_VIEW_EXCEPTION_MSG, exception);
                return null;
            }
        }
    }
}
