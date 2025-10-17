# Yandex Advertising Network Mobile
This package contains Yandex Advertising Network Mobile SDK and source code of samples of SDK usage.

## Documentation
Documentation could be found at the [official website] [DOCUMENTATION]

## License
EULA is available at [EULA website] [LICENSE]

## Quick start in Android Studio

#### 1. Import [YandexMobileAdsExample](https://github.com/yandexmobile/yandex-ads-sdk-android/tree/master/YandexMobileAdsExample)

#### 2. Build and run.

## Integration

### Configuring gradle

##### Add YandexMobileAds SDK:

```sh
implementation 'com.yandex.android:mobileads:7.16.1'
```

##### Or you can use our library with all available mediations:

##### your app module ``build.gradle ``

```sh

dependencies {
  ...
  implementation 'com.yandex.android:mobileads-mediation:7.16.1.0'
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

        // Mintegral
        maven { url 'https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea'}

        // Chartboost
        maven { url 'https://cboost.jfrog.io/artifactory/chartboost-ads/' }

        // AppNext
        maven { url 'https://dl.appnext.com/' }
    }
}
```


##### Note, for correct work of SDK you need Android Gradle Plugin version 8.3.2

[DOCUMENTATION]: https://tech.yandex.com/mobile-ads/
[LICENSE]: https://legal.yandex.com/partner_ch/
