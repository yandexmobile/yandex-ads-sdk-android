/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.video.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mobile.ads.video.RequestListener;
import com.yandex.mobile.ads.video.VideoAdError;
import com.yandex.mobile.ads.video.VideoAdRequest;
import com.yandex.mobile.ads.video.YandexVideoAds;
import com.yandex.mobile.ads.video.models.ad.VideoAd;
import com.yandex.mobile.ads.video.models.blocksinfo.BlocksInfo;

import java.io.Serializable;
import java.util.List;

public class BlockInfoActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_BLOCKS_INFO = "blocks_info";
    public static final String INTENT_EXTRA_BLOCK_ID = "block_id";

    // Pass actual pageRef and targetRef
    private static final String PAGE_REF = "https://example.com";
    private static final String TARGET_REF = "https://example.com";

    @NonNull
    private final RequestListener<List<VideoAd>> mVideoAdRequestListener = new VideoAdRequestListener();

    private View mProgress;
    private TextView mVastTextView;
    private Button mShowTrackingButton;

    @NonNull
    private List<VideoAd> mVideoAds;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_block_info);

        mProgress = findViewById(R.id.progress_video_vast);
        mVastTextView = findViewById(R.id.block_info);

        mShowTrackingButton = findViewById(R.id.show_tracking_button);
        mShowTrackingButton.setOnClickListener(mShowTracking);

        loadBlockInfo();
    }

    private final View.OnClickListener mShowTracking = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            showTracking();
        }
    };

    private void showTracking() {
        final Intent intent = new Intent(BlockInfoActivity.this, TrackingActivity.class);
        intent.putExtra(TrackingActivity.INTENT_EXTRA_VIDEO_ADS, (Serializable) mVideoAds);

        startActivity(intent);
    }

    private void loadBlockInfo() {
        final String blockId = getIntent().getStringExtra(INTENT_EXTRA_BLOCK_ID);
        final BlocksInfo blocksInfo = getIntent().getParcelableExtra(INTENT_EXTRA_BLOCKS_INFO);

        if (blocksInfo != null && blockId != null) {
            turnLoadingOn();

            final VideoAdRequest videoAdRequest =
                    new VideoAdRequest.Builder(BlockInfoActivity.this,
                            blocksInfo,
                            mVideoAdRequestListener,
                            TARGET_REF,
                            PAGE_REF,
                            blockId).build();

            YandexVideoAds.loadVideoAds(videoAdRequest);
        }else{
            Toast.makeText(BlockInfoActivity.this, "BlockInfo or BlockId is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillError(@NonNull final VideoAdError error) {
        final String errorMessage = getString(R.string.failed_to_load_vast,
                error.getCode(),
                error.getDescription());
        mVastTextView.setText(errorMessage);
    }

    private void fillVastInfo(@NonNull final String result) {
        mVastTextView.setText(result);
    }

    private void turnLoadingOn() {
        mProgress.setVisibility(View.VISIBLE);
        mVastTextView.setVisibility(View.GONE);
        mShowTrackingButton.setEnabled(false);
    }

    private void turnLoadingOff() {
        mProgress.setVisibility(View.GONE);
        mVastTextView.setVisibility(View.VISIBLE);
    }

    private class VideoAdRequestListener implements RequestListener<List<VideoAd>> {

        @Override
        public void onSuccess(final List<VideoAd> result) {
            mVideoAds = result;
            mShowTrackingButton.setEnabled(true);

            final StringBuilder resultDescription = new StringBuilder();
            for (final VideoAd videoAd : result) {
                resultDescription.append(VideoAdDescriptionProvider.getVideoDescription(videoAd));
            }
            fillVastInfo(resultDescription.toString());

            turnLoadingOff();
        }

        @Override
        public void onFailure(final VideoAdError error) {
            turnLoadingOff();
            mShowTrackingButton.setEnabled(false);
            if (error.getRawResponse() != null) {
                fillVastInfo(error.getRawResponse());
            } else {
                fillError(error);
            }
        }
    }
}
