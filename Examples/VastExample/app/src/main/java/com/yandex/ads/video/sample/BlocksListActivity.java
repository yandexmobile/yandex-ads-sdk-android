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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yandex.ads.video.sample.adapter.BlocksListAdapter;
import com.yandex.mobile.ads.video.models.blocksinfo.Block;
import com.yandex.mobile.ads.video.models.blocksinfo.BlocksInfo;

public class BlocksListActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_BLOCKS_INFO = "blocks_info";

    @Nullable
    private BlocksInfo mBlocksInfo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        mBlocksInfo = getIntent().getParcelableExtra(INTENT_EXTRA_BLOCKS_INFO);

        initAdapter();
    }

    private void initAdapter() {
        if (mBlocksInfo != null) {
            final RecyclerView mBlocksIdList = findViewById(R.id.block_ids);
            mBlocksIdList.setHasFixedSize(true);

            final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mBlocksIdList.setLayoutManager(layoutManager);

            final BlocksListAdapter mAdapter = new BlocksListAdapter(mBlocksInfo.getBlocks(), mOnBlockClickListener);
            mBlocksIdList.setAdapter(mAdapter);
        }
    }

    private final BlocksListAdapter.OnBlockClickListener mOnBlockClickListener = new BlocksListAdapter.OnBlockClickListener() {
        @Override
        public void onBlockClick(@NonNull final Block block) {
            final Intent intent = new Intent(BlocksListActivity.this, BlockInfoActivity.class);
            intent.putExtra(BlockInfoActivity.INTENT_EXTRA_BLOCK_ID, block.getId());
            intent.putExtra(BlockInfoActivity.INTENT_EXTRA_BLOCKS_INFO, mBlocksInfo);

            startActivity(intent);
        }
    };
}