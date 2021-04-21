/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.instream.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.PlayerView;
import com.yandex.instream.sample.player.SamplePlayer;
import com.yandex.instream.sample.player.ad.SampleInstreamAdPlayer;
import com.yandex.instream.sample.player.content.ContentVideoPlayer;
import com.yandex.mobile.ads.instream.InstreamAd;
import com.yandex.mobile.ads.instream.InstreamAdBreakEventListener;
import com.yandex.mobile.ads.instream.InstreamAdBreakQueue;
import com.yandex.mobile.ads.instream.InstreamAdLoadListener;
import com.yandex.mobile.ads.instream.InstreamAdLoader;
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration;
import com.yandex.mobile.ads.instream.inroll.Inroll;
import com.yandex.mobile.ads.instream.inroll.InrollQueueProvider;
import com.yandex.mobile.ads.instream.player.ad.InstreamAdView;
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener;

public class InrollActivity extends AppCompatActivity {

    /*
     * Replace demo 427408 with actual PAGE ID
     */
    private static final String PAGE_ID = "427408";

    private ContentVideoPlayer mContentVideoPlayer;

    private SampleInstreamAdPlayer mInstreamAdPlayer;

    private InstreamAdBreakQueue<Inroll> mInstreamAdBreakQueue;

    private Inroll mCurrentInroll;

    private InstreamAdView mInstreamAdView;

    private SamplePlayer mActivePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inroll);

        mInstreamAdView = findViewById(R.id.instream_ad_view);
        final PlayerView mPlayerView = findViewById(R.id.exo_player_view);

        mContentVideoPlayer = new ContentVideoPlayer(getString(R.string.content_url), mPlayerView);
        mInstreamAdPlayer = new SampleInstreamAdPlayer(mPlayerView);

        prepareVideo();
    }

    public void loadInstreamAd(@NonNull final View view) {
        InstreamAdLoader mInstreamAdLoader = new InstreamAdLoader(this);
        mInstreamAdLoader.setInstreamAdLoadListener(new AdLoaderListener());

        final InstreamAdRequestConfiguration configuration =
                new InstreamAdRequestConfiguration.Builder(PAGE_ID)
                        .build();

        mInstreamAdLoader.loadInstreamAd(this, configuration);
    }

    private void prepareVideo() {
        mContentVideoPlayer.setVideoPlayerListener(new ContentVideoPlayerListener());
        mContentVideoPlayer.prepareVideo();
    }

    public void playInroll(@NonNull final View view) {
        if (mInstreamAdBreakQueue != null) {
            mCurrentInroll = mInstreamAdBreakQueue.poll();
        }
        if (mCurrentInroll != null) {
            mCurrentInroll.setListener(new InrollListener());
            mCurrentInroll.prepare(mInstreamAdPlayer);
        }
    }

    public void pauseInroll(@NonNull final View view) {
        if (mCurrentInroll != null) {
            mCurrentInroll.pause();
        }
    }

    public void resumeInroll(@NonNull final View view) {
        if (mCurrentInroll != null) {
            mCurrentInroll.resume();
        }
    }

    private class ContentVideoPlayerListener implements VideoPlayerListener {

        @Override
        public void onVideoPrepared() {
            mContentVideoPlayer.resumeVideo();
        }

        @Override
        public void onVideoCompleted() {
            // do nothing
        }

        @Override
        public void onVideoPaused() {
            // do nothing
        }

        @Override
        public void onVideoError() {
            // do nothing
        }

        @Override
        public void onVideoResumed() {
            // do nothing
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mInstreamAdPlayer.isPlaying() || mContentVideoPlayer.isPlaying()) {
            mActivePlayer = mContentVideoPlayer.isPlaying() ? mContentVideoPlayer : mInstreamAdPlayer;
            mActivePlayer.onPause();
        } else {
            mActivePlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mActivePlayer != null) {
            mActivePlayer.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurrentInroll != null) {
            mCurrentInroll.invalidate();
            mCurrentInroll.setListener(null);
        }
    }

    private class AdLoaderListener implements InstreamAdLoadListener {

        @Override
        public void onInstreamAdLoaded(@NonNull final InstreamAd instreamAd) {
            Toast.makeText(InrollActivity.this, "onInstreamAdLoaded()", Toast.LENGTH_SHORT).show();

            final InrollQueueProvider inrollQueueProvider = new InrollQueueProvider(InrollActivity.this, instreamAd);
            mInstreamAdBreakQueue = inrollQueueProvider.getQueue();
        }

        @Override
        public void onInstreamAdFailedToLoad(@NonNull final String s) {
            Toast.makeText(InrollActivity.this, "Failed to load instream ad. Reason: " + s, Toast.LENGTH_SHORT).show();
        }
    }

    private class InrollListener implements InstreamAdBreakEventListener {

        @Override
        public void onInstreamAdBreakPrepared() {
            if (mCurrentInroll != null) {
                mCurrentInroll.play(mInstreamAdView);
            }
        }

        @Override
        public void onInstreamAdBreakStarted() {
            mContentVideoPlayer.pauseVideo();
        }

        @Override
        public void onInstreamAdBreakCompleted() {
            handleAdBreakCompleted();
        }

        @Override
        public void onInstreamAdBreakError(@NonNull final String reason) {
            handleAdBreakCompleted();
        }

        private void handleAdBreakCompleted() {
            mCurrentInroll = null;
            mContentVideoPlayer.resumeVideo();
        }
    }
}