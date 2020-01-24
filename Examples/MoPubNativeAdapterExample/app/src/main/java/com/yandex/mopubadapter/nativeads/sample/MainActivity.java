/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.mopubadapter.nativeads.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button nativeBannerExampleButton = findViewById(R.id.native_banner_example);
        nativeBannerExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MainActivity.this.startActivity(MoPubNativeBannerActivity.class);
            }
        });

        final Button nativeRecyclerViewExampleButton = findViewById(R.id.native_recycler_view_example);
        nativeRecyclerViewExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MainActivity.this.startActivity(MoPubNativeRecycleViewActivity.class);
            }
        });
    }

    private void startActivity(final Class<?> activityClass) {
        final Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }
}
