/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("plugin.serialization") version "1.8.22"
}

android {

    namespace = "com.yandex.ads.sample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yandex.ads.mobile"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "io.qameta.allure.android.runners.AllureAndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

dependencies {
    // Yandex Mobile Ads SDK with mediation adapters
    implementation("com.yandex.android:mobileads-mediation:7.16.1.0")
    implementation("com.yandex.ads.mediation:mobileads-startapp:5.0.2.17")

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("com.google.android.material:material:1.7.0")
    implementation("com.google.android.exoplayer:exoplayer:2.18.1")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.lifecycle:lifecycle-process:2.4.1")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.activity:activity-compose")
    implementation(platform("androidx.compose:compose-bom:2025.07.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("com.kaspersky.android-components:kaspresso:1.5.2")
    androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.5.2")

    androidTestUtil("androidx.test:orchestrator:1.5.1")
}
