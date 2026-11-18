# Yandex Advertising Network Mobile
This package contains Yandex Advertising Network Mobile SDK and source code of samples of SDK usage.

## Documentation
Documentation could be found at the [official website] [DOCUMENTATION]

## License
EULA is available at [EULA website] [LICENSE]

## Quick start in Android Studio

#### 1. Import [YandexMobileAdsExample](https://github.com/yandexmobile/yandex-ads-sdk-android/tree/master/YandexMobileAdsExample)

#### 2. Build and run.

> **Action required: update your Maven repository configuration**
>
> New versions of the Yandex Mobile Ads SDK for Android are no longer published to Maven Central. To receive SDK updates, add the Yandex Maven repository to your project's dependency repositories with the content filters shown below.
>
> The repository change does not change SDK artifact coordinates. Keep `google()`, `mavenCentral()`, and any repositories required by your mediation networks.

## Integration

### Configuring gradle

##### Add repositories:

Add the Yandex Maven repository to the existing `repositories` block inside `dependencyResolutionManagement` in your project's `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://ads.maven.yandex.net/")
            content {
                includeGroup("com.yandex.android")
                includeGroupAndSubgroups("com.yandex.ads")
            }
        }
    }
}
```

If your project declares dependency repositories in the root `build.gradle.kts` or `build.gradle`, add the repository and filters to the existing `allprojects { repositories { ... } }` block there. For Groovy (`build.gradle`):

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()

        maven {
            url 'https://ads.maven.yandex.net/'
            content {
                includeGroup 'com.yandex.android'
                includeGroupAndSubgroups 'com.yandex.ads'
            }
        }
    }
}
```

The repository must be available to application dependencies; configuring it only in `pluginManagement` or `buildscript` is not sufficient. Keep any repositories required by your mediation networks (listed below) in the same dependency repositories block.

See the [integration guide](https://ads.yandex.com/helpcenter/en/dev/android/quick-start) for the full setup instructions.

##### Add YandexMobileAds SDK:

```sh
implementation 'com.yandex.android:mobileads:8.7.0'
```

##### Or you can use our library with all available mediations:

##### your app module ``build.gradle ``

```sh

dependencies {
  ...
  implementation 'com.yandex.android:mobileads-mediation:8.7.0.0'
}
```

##### your project ``build.gradle ``

```sh
allprojects {
    repositories {

        ...

        // IronSource
        maven { url 'https://android-sdk.is.com/' }

        // Pangle
        maven { url 'https://artifact.bytedance.com/repository/pangle' }

        // Tapjoy
        maven { url 'https://sdk.tapjoy.com/' }

        // BidMachine
        maven { url 'https://artifactory.bidmachine.io/bidmachine' }

        // Mintegral
        maven { url 'https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea'}

        // Chartboost
        maven { url 'https://cboost.jfrog.io/artifactory/chartboost-ads/' }

        // AppNext
        maven { url 'https://dl.appnext.com/' }
    }
}
```


##### Note, for correct work of SDK you need Android Gradle Plugin version 9.0.1

[DOCUMENTATION]: https://tech.yandex.com/mobile-ads/
[LICENSE]: https://legal.yandex.com/partner_ch/
