/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.videoad.sample.adapter;

import android.support.annotation.NonNull;

import com.yandex.mobile.ads.video.models.ad.Creative;
import com.yandex.mobile.ads.video.models.ad.MediaFile;
import com.yandex.mobile.ads.video.models.ad.VideoAd;
import com.yandex.mobile.ads.video.models.common.Extension;
import com.yandex.mobile.ads.video.models.vmap.AdBreak;

import java.util.List;
import java.util.Map;

class DescriptionProvider {

    @NonNull
    String getAdBreakDescription(@NonNull final AdBreak adBreak) {
        final String extensions = getExtensionsDescription(adBreak.getExtensions());

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SourceId: ").append(adBreak.getAdSource().getId());
        stringBuilder.append("\nAllowMultipleAds: ").append(adBreak.getAdSource().isAllowMultipleAds());
        stringBuilder.append("\nFollowRedirects: ").append(adBreak.getAdSource().isFollowRedirects());
        stringBuilder.append("\nBreakId: ").append(adBreak.getBreakId());
        stringBuilder.append("\nBreakTypes: ").append(adBreak.getBreakTypes());
        stringBuilder.append("\nRepeatAfter in millis: ").append(adBreak.getRepeatAfterMillis());
        stringBuilder.append("\nTimeOffset: ").append(adBreak.getTimeOffset().getRawValue());
        stringBuilder.append(extensions);

        return stringBuilder.toString();
    }

    @NonNull
    String getExtensionsDescription(@NonNull final List<Extension> extensions) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (extensions.isEmpty() == false) {
            stringBuilder.append("\n\nExtensions:\n");
            for (final Extension extension : extensions) {
                stringBuilder.append(extension.getType()).append(": ").append(extension.getValue()).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    @NonNull
    String getTrackingEventsDescription(@NonNull final Map<String, List<String>> trackingEvents) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (trackingEvents.isEmpty() == false) {
            stringBuilder.append("\n\nTrackingEvents:")
                    .append(trackingEvents.keySet().toString());
        }

        return stringBuilder.toString();
    }

    @NonNull
    String getVideoAdDescription(@NonNull final VideoAd videoAd) {
        final Map<String, List<String>> adTrackingEvents = videoAd.getTrackingEvents();
        final String trackingEvents = getTrackingEventsDescription(adTrackingEvents);

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(videoAd.getAdTitle());
        stringBuilder.append("\nSystem: ").append(videoAd.getAdSystem());
        stringBuilder.append("\nSurvey: ").append(videoAd.getSurvey());
        stringBuilder.append("\nVastAdTagUri: ").append(videoAd.getVastAdTagUri());
        stringBuilder.append(trackingEvents);

        final List<Creative> creatives = videoAd.getCreatives();
        if (creatives.isEmpty() == false) {
            stringBuilder.append("\n\nCreatives:\n");
            for (final Creative creative : videoAd.getCreatives()) {
                stringBuilder.append("\nId: ").append(creative.getId());
                stringBuilder.append("\nDuration: ").append(creative.getDurationMillis());
                stringBuilder.append("\nClickThroughUrl: ").append(creative.getClickThroughUrl());

                final List<MediaFile> mediaFiles = creative.getMediaFiles();
                if (mediaFiles.isEmpty() == false) {
                stringBuilder.append("\n\nMediaFiles:\n");
                    for (final MediaFile mediaFile : mediaFiles) {
                        stringBuilder.append("Id: ").append(mediaFile.getId());
                        stringBuilder.append("\nUri: ").append(mediaFile.getUri());
                        stringBuilder.append("\nBitrate: ").append(mediaFile.getBitrate());
                        stringBuilder.append("\nDeliveryMethod: ").append(mediaFile.getDeliveryMethod());
                        stringBuilder.append("\nSize: (").append(mediaFile.getWidth())
                                .append(", ").append(mediaFile.getHeight()).append(")");
                        stringBuilder.append("\nMimeType: ").append(mediaFile.getMimeType());
                    }
                }

                final String creativeTrackingEvents = getTrackingEventsDescription(creative.getTrackingEvents());
                stringBuilder.append(creativeTrackingEvents);
            }
        }

        return stringBuilder.toString();
    }
}
