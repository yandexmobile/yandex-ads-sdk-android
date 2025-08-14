/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.9.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {

        google()
        mavenCentral()

        // IronSource
        maven { url = uri("https://android-sdk.is.com/") }

        // Pangle
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }

        // Tapjoy
        maven { url = uri("https://sdk.tapjoy.com/") }

        // Mintegral
        maven { url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea") }

        // Chartboost
        maven { url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads/") }

        // AppNext
        maven { url = uri("https://dl.appnext.com/") }

        // PetalAds
        maven { url = uri("https://developer.huawei.com/repo/") }
    }
}
