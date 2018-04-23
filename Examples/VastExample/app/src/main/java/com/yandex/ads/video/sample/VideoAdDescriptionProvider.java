/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.video.sample;

import android.support.annotation.NonNull;

import com.yandex.mobile.ads.video.models.ad.Creative;
import com.yandex.mobile.ads.video.models.ad.MediaFile;
import com.yandex.mobile.ads.video.models.ad.VideoAd;

class VideoAdDescriptionProvider {

    @NonNull
    static String getVideoDescription(@NonNull final VideoAd videoAd) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(videoAd.getAdTitle());
        stringBuilder.append("\nSystem: ").append(videoAd.getAdSystem());
        stringBuilder.append("\nSurvey: ").append(videoAd.getSurvey());
        stringBuilder.append("\nVastAdTagUri: ").append(videoAd.getVastAdTagUri());
        stringBuilder.append("\nCreatives:\n");

        for (final Creative creative : videoAd.getCreatives()) {
            stringBuilder.append("\n\tDuration: ").append(creative.getDurationMillis());
            stringBuilder.append("\n\tClickThroughUrl: ").append(creative.getClickThroughUrl());
            stringBuilder.append("\n\tMediaFiles:\n");

            for (final MediaFile mediaFile : creative.getMediaFiles()) {
                stringBuilder.append("\n\t\tDuration: ").append(mediaFile.getDeliveryMethod());
                stringBuilder.append("\n\t\tSize: (").append(mediaFile.getWidth())
                        .append(", ").append(mediaFile.getHeight()).append(")");
                stringBuilder.append("\n\t\tMimeType: ").append(mediaFile.getMimeType());
            }
        }
        return stringBuilder.toString();
    }
}
