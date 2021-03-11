/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2019 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.videoad.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.ads.videoad.sample.R;
import com.yandex.mobile.ads.video.VmapError;
import com.yandex.mobile.ads.video.VmapLoader;
import com.yandex.mobile.ads.video.VmapRequestConfiguration;
import com.yandex.mobile.ads.video.models.vmap.AdBreak;
import com.yandex.mobile.ads.video.models.vmap.Vmap;
import com.yandex.videoad.sample.adapter.DividerItemDecoration;
import com.yandex.videoad.sample.adapter.VmapAdapter;

public class MainActivity extends AppCompatActivity {

    /*
     * Replace demo PAGE_ID with your actual Page Id
     */
    public static final String PAGE_ID = "349941";
    public static final String CATEGORY_ID = "0";

    private VmapLoader mVmapLoader;
    private VmapAdapter mVmapAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVmapLoader = new VmapLoader(this);
        mVmapLoader.setOnVmapLoadedListener(new VmapLoadedListener());

        final RecyclerView vmapInfoView = findViewById(R.id.vmap);
        vmapInfoView.setLayoutManager(new LinearLayoutManager(this));
        vmapInfoView.addItemDecoration(new DividerItemDecoration(this));
        vmapInfoView.setHasFixedSize(true);

        mVmapAdapter = new VmapAdapter(new AdBreakClickListener());
        vmapInfoView.setAdapter(mVmapAdapter);
    }

    public void loadVmap(final View view) {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        /*
         * Replace demo PAGE_ID with actual Page Id
         */
        final VmapRequestConfiguration vmapRequestConfiguration =
                new VmapRequestConfiguration.Builder(PAGE_ID).setCategory(CATEGORY_ID).build();
        mVmapLoader.loadVmap(MainActivity.this, vmapRequestConfiguration);
    }

    private class VmapLoadedListener extends VmapLoader.OnVmapLoadedListener {

        @Override
        public void onVmapLoaded(@NonNull final Vmap vmap) {
            mVmapAdapter.setData(vmap);
            hideProgressBar();
        }

        @Override
        public void onVmapFailedToLoad(@NonNull final VmapError error) {
            hideProgressBar();
            Toast.makeText(MainActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
        }

        private void hideProgressBar() {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }
    }

    private class AdBreakClickListener implements VmapAdapter.OnAdBreakClickListener {
        @Override
        public void onAdBreakClick(@NonNull final AdBreak adBreak) {
            final Intent intent = new Intent(MainActivity.this, VideoAdLoadingActivity.class);
            intent.putExtra(VideoAdLoadingActivity.EXTRA_AD_BREAK, adBreak);

            startActivity(intent);
        }
    }
}
