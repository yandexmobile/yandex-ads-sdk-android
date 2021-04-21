/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2021 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.instream.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectInstream(@NonNull final View view) {
        final Intent intent = new Intent(this, InstreamActivity.class);
        startActivity(intent);
    }

    public void selectInroll(@NonNull final View view) {
        final Intent intent = new Intent(this, InrollActivity.class);
        startActivity(intent);
    }
}