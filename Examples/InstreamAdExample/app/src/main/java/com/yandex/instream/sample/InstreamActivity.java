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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.PlayerView;
import com.yandex.instream.sample.player.SamplePlayer;
import com.yandex.instream.sample.player.ad.SampleInstreamAdPlayer;
import com.yandex.instream.sample.player.content.ContentVideoPlayer;
import com.yandex.mobile.ads.instream.InstreamAd;
import com.yandex.mobile.ads.instream.InstreamAdBinder;
import com.yandex.mobile.ads.instream.InstreamAdListener;
import com.yandex.mobile.ads.instream.InstreamAdLoadListener;
import com.yandex.mobile.ads.instream.InstreamAdLoader;
import com.yandex.mobile.ads.instream.InstreamAdRequestConfiguration;
import com.yandex.mobile.ads.instream.player.ad.InstreamAdView;

public class InstreamActivity extends AppCompatActivity {

    private static final String PAGE_ID = "R-M-DEMO-instream-vmap";

    private InstreamAdView mInstreamAdView;

    private ContentVideoPlayer mContentVideoPlayer;

    private SampleInstreamAdPlayer mInstreamAdPlayer;

    private InstreamAdLoader mInstreamAdLoader;

    private InstreamAdBinder mInstreamAdBinder;

    private SamplePlayer mActivePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instream);

        mInstreamAdView = findViewById(R.id.instream_ad_view);

        final PlayerView exoPlayerView = findViewById(R.id.exo_player_view);
        mContentVideoPlayer = new ContentVideoPlayer(getString(R.string.content_url), exoPlayerView);
        mInstreamAdPlayer = new SampleInstreamAdPlayer(exoPlayerView);

        loadInstreamAd();
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
        if (mInstreamAdBinder != null) {
            mInstreamAdBinder.unbind();
        }

        mContentVideoPlayer.onDestroy();
        mInstreamAdPlayer.onDestroy();

        super.onDestroy();
    }

    private void loadInstreamAd() {
        mInstreamAdLoader = new InstreamAdLoader(this);
        mInstreamAdLoader.setInstreamAdLoadListener(new AdLoaderListener());

        // Replace demo PAGE_ID with actual received in partner interface
        final InstreamAdRequestConfiguration configuration =
                new InstreamAdRequestConfiguration.Builder(PAGE_ID).build();
        mInstreamAdLoader.loadInstreamAd(this, configuration);
    }

    private void showInstreamAd(@NonNull final InstreamAd instreamAd) {
        mInstreamAdBinder = new InstreamAdBinder(InstreamActivity.this, instreamAd, mInstreamAdPlayer, mContentVideoPlayer);
        mInstreamAdBinder.setInstreamAdListener(new InstreamAdPlaybackListener());
        mInstreamAdBinder.bind(mInstreamAdView);
    }

    private class AdLoaderListener implements InstreamAdLoadListener {

        @Override
        public void onInstreamAdLoaded(@NonNull final InstreamAd instreamAd) {
            showInstreamAd(instreamAd);
        }

        @Override
        public void onInstreamAdFailedToLoad(@NonNull final String reason) {
            final String msg = "Failed to present instream ad. Reason: " + reason;
            Toast.makeText(InstreamActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class InstreamAdPlaybackListener implements InstreamAdListener {

        @Override
        public void onInstreamAdCompleted() {
            Toast.makeText(InstreamActivity.this, "Instream ad completed.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInstreamAdPrepared() {
            // do nothing
        }

        @Override
        public void onError(@NonNull final String reason) {
            final String msg = "Failed to present instream ad. Reason: " + reason;
            Toast.makeText(InstreamActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
