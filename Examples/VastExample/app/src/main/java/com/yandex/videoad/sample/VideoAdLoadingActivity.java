/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.videoad.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.ads.videoad.sample.R;
import com.yandex.mobile.ads.video.VastRequestConfiguration;
import com.yandex.mobile.ads.video.VideoAdError;
import com.yandex.mobile.ads.video.VideoAdLoader;
import com.yandex.mobile.ads.video.models.ad.Creative;
import com.yandex.mobile.ads.video.models.ad.VideoAd;
import com.yandex.mobile.ads.video.models.vmap.AdBreak;
import com.yandex.mobile.ads.video.tracking.Tracker;
import com.yandex.videoad.sample.adapter.DividerItemDecoration;
import com.yandex.videoad.sample.adapter.VideoAdAdapter;

import java.util.List;

public class VideoAdLoadingActivity extends AppCompatActivity {

    static final String EXTRA_AD_BREAK = "VideoAdLoadingActivity.ad_break_extra";

    private VideoAdLoader mVideoAdLoader;
    private VideoAdAdapter mVideoAdAdapter;

    private AdBreak mAdBreak;
    private Tracker mTracker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast);

        mTracker = new Tracker(this);
        mAdBreak = getIntent().getParcelableExtra(EXTRA_AD_BREAK);

        mVideoAdLoader = new VideoAdLoader(this);
        mVideoAdLoader.setOnVideoAdLoadedListener(new VideoAdLoadedListener());

        final RecyclerView vastInfo = findViewById(R.id.vast);
        vastInfo.setLayoutManager(new LinearLayoutManager(this));
        vastInfo.addItemDecoration(new DividerItemDecoration(this));
        vastInfo.setHasFixedSize(true);

        mVideoAdAdapter = new VideoAdAdapter(new TrackingEventListener());
        vastInfo.setAdapter(mVideoAdAdapter);

        loadVast(findViewById(R.id.load_vast));
    }

    public void loadVast(@NonNull final View view) {
        if (mAdBreak != null) {
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

            final VastRequestConfiguration vastRequestConfiguration =
                    new VastRequestConfiguration.Builder(mAdBreak).build();
            mVideoAdLoader.loadAd(this, vastRequestConfiguration);
        }
    }

    private class VideoAdLoadedListener extends VideoAdLoader.OnVideoAdLoadedListener {
        @Override
        public void onVideoAdLoaded(@NonNull final List<VideoAd> videoAds) {
            mVideoAdAdapter.setData(videoAds);
            hideProgressBar();
        }

        @Override
        public void onVideoAdFailedToLoad(@NonNull final VideoAdError error) {
            hideProgressBar();
            Toast.makeText(VideoAdLoadingActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
        }

        private void hideProgressBar() {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }
    }

    private class TrackingEventListener implements VideoAdAdapter.TrackingEventListener {
        @Override
        public void trackEvents(@NonNull final VideoAd videoAd) {
            final String event = "start";
            mTracker.trackAdEvent(videoAd, event);
            Toast.makeText(VideoAdLoadingActivity.this, "Track Ad's events with name = " + event, Toast.LENGTH_SHORT).show();

            for (final Creative creative : videoAd.getCreatives()) {
                mTracker.trackCreativeEvent(creative, event);
                Toast.makeText(VideoAdLoadingActivity.this, "Track Creative's events with name = " + event, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
