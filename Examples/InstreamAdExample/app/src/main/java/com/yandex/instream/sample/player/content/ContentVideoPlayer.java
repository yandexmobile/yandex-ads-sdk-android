/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.instream.sample.player.content;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.yandex.instream.sample.player.SamplePlayer;
import com.yandex.mobile.ads.instream.player.content.VideoPlayer;
import com.yandex.mobile.ads.instream.player.content.VideoPlayerListener;

public class ContentVideoPlayer implements VideoPlayer, SamplePlayer {

    private final String mVideoUrl;

    private final PlayerView mExoPlayerView;

    private final SimpleExoPlayer mExoPlayer;

    private VideoPlayerListener mVideoPlayerListener;

    public ContentVideoPlayer(final String videoUrl,
                              final PlayerView exoplayerView) {
        mVideoUrl = videoUrl;
        mExoPlayerView = exoplayerView;

        mExoPlayer = new SimpleExoPlayer.Builder(exoplayerView.getContext()).build();
        mExoPlayer.addListener(new ContentPlayerEventsListener());
    }

    @Override
    public boolean isPlaying() {
        return mExoPlayer.isPlaying();
    }

    @Override
    public void onResume() {
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        mExoPlayer.setPlayWhenReady(false);
    }

    public void onDestroy() {
        mExoPlayer.release();
    }

    @Override
    public void prepareVideo() {
        final MediaSource videoSource = getMediaSource(mExoPlayerView.getContext(), mVideoUrl);

        mExoPlayer.setPlayWhenReady(false);
        mExoPlayer.addListener(new Player.EventListener() {

            @Override
            public void onPlayerStateChanged(final boolean playWhenReady,
                                             final int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    if (mVideoPlayerListener != null) {
                        mVideoPlayerListener.onVideoPrepared();
                    }

                    mExoPlayer.removeListener(this);
                }
            }
        });

        mExoPlayer.prepare(videoSource, true, true);
    }

    @NonNull
    private MediaSource getMediaSource(@NonNull final Context context,
                                       @NonNull final String url) {
        final String userAgent = Util.getUserAgent(context, "Content video player");
        final DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);

        final Uri contentUri = Uri.parse(url);
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(contentUri);
    }

    @Override
    public long getVideoPosition() {
        return mExoPlayer.getCurrentPosition();
    }

    @Override
    public long getVideoDuration() {
        return mExoPlayer.getDuration();
    }

    @Override
    public float getVolume() {
        return mExoPlayer.getVolume();
    }

    @Override
    public void pauseVideo() {
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void resumeVideo() {
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayerView.setUseController(true);

        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void setVideoPlayerListener(@Nullable final VideoPlayerListener videoPlayerListener) {
        mVideoPlayerListener = videoPlayerListener;
    }

    private class ContentPlayerEventsListener implements Player.EventListener {

        @Override
        public void onIsPlayingChanged(final boolean isPlaying) {
            if (mVideoPlayerListener != null) {
                if (isPlaying) {
                    mVideoPlayerListener.onVideoResumed();
                } else {
                    mVideoPlayerListener.onVideoPaused();
                }
            }
        }

        @Override
        public void onPlayerStateChanged(final boolean playWhenReady,
                                         final int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                if (mVideoPlayerListener != null) {
                    mVideoPlayerListener.onVideoCompleted();
                }
            }
        }

        @Override
        public void onPlayerError(final ExoPlaybackException error) {
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoError();
            }
        }
    }
}
