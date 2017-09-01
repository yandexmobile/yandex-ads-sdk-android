/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2017 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.adfox.nativeads.sample;

import com.yandex.metrica.YandexMetrica;

public class MyApplication extends android.app.Application {

    private static final String API_KEY = "43614695-4bad-431c-9e14-fa588179b756";

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        * Replace API_KEY with your unique API key. Please, read official documentation how to obtain one:
        * {@link https://tech.yandex.com/metrica-mobile-sdk/doc/mobile-sdk-dg/concepts/android-initialize-docpage}
        */
        YandexMetrica.activate(getApplicationContext(), API_KEY);
    }
}
