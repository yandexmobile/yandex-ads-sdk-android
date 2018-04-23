/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.video.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.yandex.ads.video.sample.adapter.TrackingAdapter;
import com.yandex.ads.video.sample.model.TrackingModel;
import com.yandex.mobile.ads.video.VideoAdError;
import com.yandex.mobile.ads.video.models.ad.Creative;
import com.yandex.mobile.ads.video.models.ad.VideoAd;
import com.yandex.mobile.ads.video.tracking.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackingActivity extends AppCompatActivity {

    private static final String TAG = "TrackingActivity";

    public static final String INTENT_EXTRA_VIDEO_ADS = "video_ads";

    @NonNull
    private final TrackingErrorListener mTrackingErrorListener = new TrackingErrorListener();

    @NonNull
    private final List<TrackingModel> mTrackingItems = new ArrayList<>();

    @NonNull
    private Tracker mTracker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        initTracker();
        initTrackEventList();
        initAdapter();
    }

    private void initTracker() {
        mTracker = new Tracker(TrackingActivity.this);
        mTracker.setErrorListener(mTrackingErrorListener);
    }

    private void initTrackEventList() {
        final List<VideoAd> mVideoAds =
                (List<VideoAd>) getIntent().getSerializableExtra(INTENT_EXTRA_VIDEO_ADS);

        if (mVideoAds != null) {
            for (final VideoAd ad : mVideoAds) {
                addTrackingItems(ad, TrackingModel.Type.AD, ad.getTrackingEvents());
                final List<Creative> creatives = ad.getCreatives();

                for (final Creative creative : creatives) {
                    addTrackingItems(creative, TrackingModel.Type.CREATIVE, creative.getTrackingEvents());
                }
            }
        }
    }

    private void addTrackingItems(@NonNull final Object item,
                                  @NonNull final TrackingModel.Type type,
                                  @NonNull final Map<String, List<String>> trackingEvents) {
        for (final Map.Entry<String, List<String>> trackingLinksForEvent : trackingEvents.entrySet()) {
            final String eventName = trackingLinksForEvent.getKey();
            if (trackingLinksForEvent.getValue().size() > 0) {
                mTrackingItems.add(
                        new TrackingModel(type, type.toString() + "_" + eventName.toUpperCase(), item));
            }
        }
    }

    private void initAdapter() {
        final RecyclerView mTrackingList = findViewById(R.id.tracking_items);
        mTrackingList.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mTrackingList.setLayoutManager(layoutManager);

        final TrackingAdapter mAdapter = new TrackingAdapter(mTrackingItems, onTrackingItemClickListener);
        mTrackingList.setAdapter(mAdapter);
    }

    private final TrackingAdapter.OnTrackingItemClickListener onTrackingItemClickListener = new TrackingAdapter.OnTrackingItemClickListener() {
        @Override
        public void onTrackingItemClick(@NonNull final TrackingModel model) {
            final String eventName = model.getEventName();
            Toast.makeText(TrackingActivity.this, eventName, Toast.LENGTH_SHORT).show();

            if (model.getType() == TrackingModel.Type.AD) {
                mTracker.trackAdEvent((VideoAd) model.getItem(), eventName);
            } else {
                mTracker.trackCreativeEvent((Creative) model.getItem(), eventName);
            }
        }
    };

    private class TrackingErrorListener implements Tracker.ErrorListener {
        @Override
        public void onTrackingError(@NonNull final VideoAdError error) {
            final String errorMessage = getString(R.string.failed_to_track_event,
                    String.valueOf(error.getCode()),
                    error.getDescription());

            Log.e(TAG, errorMessage);
            Toast.makeText(TrackingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}