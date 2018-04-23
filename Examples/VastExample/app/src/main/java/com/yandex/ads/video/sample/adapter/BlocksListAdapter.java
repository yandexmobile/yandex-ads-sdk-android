/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.video.sample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yandex.ads.video.sample.R;
import com.yandex.mobile.ads.video.models.blocksinfo.Block;

import java.util.List;

public class BlocksListAdapter extends RecyclerView.Adapter<BlocksListAdapter.ViewHolder> {

    public interface OnBlockClickListener {
        void onBlockClick(@NonNull final Block block);
    }

    @NonNull
    private final List<Block> mBlocks;

    @NonNull
    private final OnBlockClickListener mListener;

    public BlocksListAdapter(@NonNull final List<Block> blocks,
                             @NonNull final OnBlockClickListener listener) {
        mBlocks = blocks;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Block block = mBlocks.get(position);
        holder.fillHolder(block, mListener);
    }

    @Override
    public int getItemCount() {
        return mBlocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mId;
        final TextView mType;
        final TextView mStartTime;
        final TextView mDuration;
        final TextView mPositionsCount;

        final Button mShowButton;

        ViewHolder(final View itemView) {
            super(itemView);
            mId = itemView.findViewById(R.id.tv_id_text);
            mType = itemView.findViewById(R.id.tv_type_text);
            mStartTime = itemView.findViewById(R.id.tv_start_time_text);
            mDuration = itemView.findViewById(R.id.tv_duration_text);
            mPositionsCount = itemView.findViewById(R.id.tv_positions_count_text);
            mShowButton = itemView.findViewById(R.id.id_show_button);
        }

        void fillHolder(@NonNull final Block block,
                        @NonNull final OnBlockClickListener listener) {
            mId.setText(block.getId());
            mType.setText(String.valueOf(block.getType()));
            mStartTime.setText(String.valueOf(block.getStartTime()));
            mDuration.setText(String.valueOf(block.getDuration()));
            mPositionsCount.setText(String.valueOf(block.getPositionsCount()));

            mShowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    listener.onBlockClick(block);
                }
            });
        }
    }
}