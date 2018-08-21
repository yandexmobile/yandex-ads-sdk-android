/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2018 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */
package com.yandex.adfox.mediation.interstitial.sample;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

class MediationConfigurator {

    static final Map<String, String> ADFOX_DEMO_INTERSTITIAL = createAdFoxParameters();
    static final Map<String, String> ADMOB_DEMO_INTERSTITIAL = createAdMobParameters();
    static final Map<String, String> FACEBOOK_DEMO_INTERSTITIAL = createFacebookParameters();
    static final Map<String, String> MOPUB_DEMO_INTERSTITIAL = createMoPubParameters();
    static final Map<String, String> MYTARGET_DEMO_INTERSTITIAL = createMyTargetParameters();
    static final Map<String, String> STARTAPP_DEMO_INTERSTITIAL = createStartAppParameters();

    @NonNull
    private static Map<String, String> createAdFoxParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "270901");
        parameters.put("adf_p1", "cawxi");
        parameters.put("adf_p2", "fkbc");
        parameters.put("adf_pt", "b");

        return parameters;
    }

    @NonNull
    private static Map<String, String> createAdMobParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "270901");
        parameters.put("adf_p1", "caayb");
        parameters.put("adf_p2", "fhmf");
        parameters.put("adf_pt", "b");

        return parameters;
    }

    @NonNull
    private static Map<String, String> createFacebookParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "270901");
        parameters.put("adf_p1", "caalz");
        parameters.put("adf_p2", "fhmf");
        parameters.put("adf_pt", "b");

        return parameters;
    }

    @NonNull
    private static Map<String, String> createMoPubParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "270901");
        parameters.put("adf_p1", "caama");
        parameters.put("adf_p2", "fhmf");
        parameters.put("adf_pt", "b");

        return parameters;
    }

    @NonNull
    private static Map<String, String> createMyTargetParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "270901");
        parameters.put("adf_p1", "caamb");
        parameters.put("adf_p2", "fhmf");
        parameters.put("adf_pt", "b");

        return parameters;
    }

    @NonNull
    private static Map<String, String> createStartAppParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("adf_ownerid", "270901");
        parameters.put("adf_p1", "caamc");
        parameters.put("adf_p2", "fhmf");
        parameters.put("adf_pt", "b");

        return parameters;
    }
}
