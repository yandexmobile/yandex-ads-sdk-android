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

import com.yandex.mobile.ads.video.BlocksInfoRequest;
import com.yandex.mobile.ads.video.RequestListener;
import com.yandex.mobile.ads.video.VideoAdError;
import com.yandex.mobile.ads.video.YandexVideoAds;
import com.yandex.mobile.ads.video.models.blocksinfo.BlocksInfo;

public class MainActivity extends AppCompatActivity {

    // Pass actual partnerId and categoryId
    private static final String PARTNER_ID = "111111";
    private static final String CATEGORY_ID = "0";

    @NonNull
    private BlocksInfo mBlocksInfo;

    private View mProgress;
    private TextView mTvBlocksInfo;
    private Button mLoadButton;
    private Button mShowAllBlocksButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = findViewById(R.id.progress_blocks_info);
        mTvBlocksInfo = findViewById(R.id.blocks_info);

        mLoadButton = findViewById(R.id.load_ad_button);
        mLoadButton.setOnClickListener(mGetBlocksInfo);

        mShowAllBlocksButton = findViewById(R.id.show_blocks_button);
        mShowAllBlocksButton.setOnClickListener(mShowAllBlocks);
    }

    private final View.OnClickListener mGetBlocksInfo = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            turnLoadingOn();
            loadAd();
        }
    };

    private final View.OnClickListener mShowAllBlocks = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            showAllBlocks();
        }
    };

    private void showAllBlocks() {
        final Intent intent = new Intent(MainActivity.this, BlocksListActivity.class);
        intent.putExtra(BlocksListActivity.INTENT_EXTRA_BLOCKS_INFO, mBlocksInfo);

        startActivity(intent);
    }

    private void loadAd() {
        final BlocksInfoRequest.Builder request = new BlocksInfoRequest
                .Builder(MainActivity.this, PARTNER_ID, mBlocksInfoRequestListener)
                .setCategory(CATEGORY_ID);

        YandexVideoAds.loadBlocksInfo(request.build());
    }

    private final RequestListener<BlocksInfo> mBlocksInfoRequestListener = new RequestListener<BlocksInfo>() {
        @Override
        public void onSuccess(@NonNull final BlocksInfo result) {
            mBlocksInfo = result;

            turnLoadingOff();
            fillBlocksInfo(result);
        }

        @Override
        public void onFailure(@NonNull final VideoAdError error) {
            turnLoadingOff();
            fillError(error);
        }
    };

    private void fillBlocksInfo(@NonNull final BlocksInfo blocksInfo) {
        mShowAllBlocksButton.setEnabled(true);
        mTvBlocksInfo.setText(blocksInfo.toString());
    }

    private void fillError(@NonNull final VideoAdError error) {
        mShowAllBlocksButton.setEnabled(false);
        final String errorMessage = getString(R.string.failed_to_load_blocks_info,
                error.getCode(),
                error.getDescription());
        mTvBlocksInfo.setText(errorMessage);
    }

    private void turnLoadingOn() {
        mProgress.setVisibility(View.VISIBLE);
        mTvBlocksInfo.setVisibility(View.GONE);
        mLoadButton.setEnabled(false);
        mShowAllBlocksButton.setEnabled(false);
    }

    private void turnLoadingOff() {
        mProgress.setVisibility(View.GONE);
        mTvBlocksInfo.setVisibility(View.VISIBLE);
        mLoadButton.setEnabled(true);
    }
}