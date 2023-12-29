# Third-party mediation adapter template

## Description

The code template found in this folder can be used to create an adapter for embedding the [Yandex Advertising Network](https://yandex.ru/support2/mobile-ads/en) into Third-Party Mediation. The template contains two files:
* The [MediationAPI](./app/src/main/java/com/example/mediation/MediationAPI.kt) file contains stub interfaces and classes that describes an API of your Mediation SDK. This file is just an example and your actual API may be different from this one.
* The [YandexAdapters](./app/src/main/java/com/example/mediation/YandexAdapters.kt) file contains classes that implements [MediationAPI](./app/src/main/java/com/example/mediation/MediationAPI.kt) to integrate [Yandex Advertising Network](https://yandex.ru/support2/mobile-ads/en) into your Mediation.

> Here is a quick start for writing an adapter. Full documentation for Yandex Mobile Ads SDK can be found on the [official website](https://yandex.ru/support2/mobile-ads/en/dev/android).


## Getting started

### 1. App requirements

* Use Android Studio 2021 or later.
* Make sure your app's build file uses the following values:
    * `minSdkVersion` 21 or later.
    * `compileSdkVersion` 31 or later.

### 2. Yandex Mobile Ads SDK dependency

> You can check the latest Yandex Mobile SDK version [here](https://yandex.ru/support2/mobile-ads/en/dev/platforms).

```kotlin
dependencies {
    implementation("com.yandex.android:mobileads:$YANDEX_SDK_VERSION")
}
```

See [also](https://yandex.ru/support2/mobile-ads/en/dev/android/quick-start#app).

### 3. Implement adapter

Copy [YandexAdapters](./app/src/main/java/com/example/mediation/YandexAdapters.kt) file content and change stub API to your actual API.
This way you will get a separate adapter class for each of the ad formats. If your API requires a single class for all formats, you can merge classes.

* Mediation parameters must be set for each request as shown in the [template](./app/src/main/java/com/example/mediation/YandexAdapters.kt#L631):
    * `"adapter_version"` represents Yandex adapter version. Construct this version by adding one more number to the Yandex SDK version. For example, `6.2.1.0` if the Yandex SDK version is `6.2.1`. If you need to update an adapter without changing the Yandex SDK version, increment the fourth number like `6.2.1.1`.
    * `"adapter_network_name"` represents your ad network name in lowercase.
    * `"adapter_network_sdk_version"` represents your ad network SDK version.

* `Interstitial`, `Rewarded`, `AppOpen` and `Native` formats provide loader classes. A loader object can be [created](./app/src/main/java/com/example/mediation/YandexAdapters.kt#L60) once and can be reused. This speeds up loading and can be helpful to implement preloading logic, if your network supports it.

* `Native` format includes both the required and optional [assets](https://yandex.ru/support2/mobile-ads/en/dev/android/components). The way to provide custom assets for binding strongly depends on the API of your ads SDK. The [template](./app/src/main/java/com/example/mediation/YandexAdapters.kt#L690) shows how these assets are passed to adapter during the ad request by providing a map of `string identifier` to `asset view identifier` on the publisher side.

### 4. Test integration

It is recommended to use test ads to check your adapter. These special demo ad unit identifiers guarantee successful ad response for each request:

* AppOpen format: `demo-appopenad-yandex`
* Banner format: `demo-banner-yandex`
* Interstitial format: `demo-interstitial-yandex`
* Native format: `demo-native-content-yandex`, `demo-native-app-yandex`, `demo-native-video-yandex`
* Rewarded format: `demo-rewarded-yandex`

## Additional info

### Initialization

Successfully initializing the Yandex Mobile Ads SDK is an important condition for correctly integrating the library. By default, the SDK is initialized automatically at an app startup. You can disable automatic initialization:
```xml
<manifest>
    <application>
        <!-- Disable automatic sdk initialization.  -->
        <meta-data
            android:name="com.yandex.mobile.ads.AUTOMATIC_SDK_INITIALIZATION"
            android:value="false" />
    </application>
</manifest>
```

Manual initialization of the SDK can be used like this (this method is safety and can be reinvoked if SDK already initialized):
```kotlin
MobileAds.initialize(context) {
    onInitializationComplete.invoke()
}
```

### Privacy policies

Privacy policies can be configured like this:
```kotlin
MobileAds.setAgeRestrictedUser(params.isAgeRestrictedUser())
MobileAds.setLocationConsent(params.hasLocationConsent())
MobileAds.setUserConsent(params.hasUserConsent())
```
> You should configure policies before initializing the SDK or reinitialize after changing them.

See also: [GDPR](https://yandex.ru/support2/mobile-ads/en/dev/android/gdpr) and [COPPA](https://yandex.ru/support2/mobile-ads/en/dev/android/coppa).

### S2S bidding integration

As shown in the [template](./app/src/main/java/com/example/mediation/YandexAdapters.kt#L664), the bidder token can be obtained as follows:

```kotlin
val loadListener = object : BidderTokenLoadListener {
    override fun onBidderTokenLoaded(token: String) = callback.onTokenLoaded(token)
    override fun onBidderTokenFailedToLoad(msg: String) = callback.onTokenFailedToLoad()
}

BidderTokenLoader.loadBidderToken(context, tokenRequest, loadListener)
```

You need to load a bidder token for each new ad request. Token request can be created as follows (also shown in [template]((./app/src/main/java/com/example/mediation/YandexAdapters.kt#L611))):

```kotlin
fun createBidderTokenRequestConfiguration(): BidderTokenRequestConfiguration {
        val requestBuilder = BidderTokenRequestConfiguration.Builder(adType)

        if (adType == AdType.BANNER) {
            requestBuilder.setBannerAdSize(bannerAdSize)
        }

        return requestBuilder
            .setParameters(mediationParameters)
            .build()
    }
```
