# Change Log

All notable changes to Yandex Mobile Ads SDK will be documented in this file.

## Version 6.3.0

#### Added
* Improvements and optimizations

## Version 6.2.0

#### Added
* Additional parameters for Open Bidding token generator

#### Updated
* AppMetrica SDK version updated to 6.0.0
* DivKit SDK version updated to 28.9.0

#### Fixed
* Fix callback for multi ads design

## Version 6.1.0

#### Added
* New video ads design
* Improved ads loading caching

#### Fixed
* Fixed Instream ads skin localization
* Instream ads navigation on Android TV

## Version 6.0.1

### Fixed
* Fixed handling of incompatible mediation adapters

## Version 6.0.0

### Added
* App Open Ad format
* New ad formats in rewarded
* Ability to close rewarded ad before reward
* Improvements and optimizations

### Breaking changes
* New banner ad size API
* Interstitial ad loading and ad show API decomposition
* Rewarded ad loading and ad show API decomposition
* See [migration guide](https://yandex.ru/support2/mobile-ads/ru/dev/android/release/6-0-0-migration)

## Version 5.10.0

#### Added
* Improvements and optimizations

## Version 5.9.0

#### Added
* Improvements and optimizations

#### Updated
* DivKit version updated to 25.2.0

## Version 5.8.0

#### Added
* Improved logs for SDK correct integration check
* Improvements and optimizations

#### Updated
* DivKit version updated to 24.3.0

## Version 5.7.0

#### Added

* Logs for SDK correct integration check
* Improvements and optimizations

## Version 5.6.0

#### Updated

* Kotlin Standard Library updated to 1.7.10

#### Fixed

* Fixed class VerifyError during dex2oat processing

## Version 5.5.1

#### Added

* Improvements and optimizations

## Version 5.5.0

#### Added

* Added explicit check for SDK API call just from the main thread
* Improvements and optimizations

#### Fixed

* Fixed possible crash during ad video playback

## Version 5.4.1

#### Fixed

* Fixed feedback drawing on miui

## Version 5.4.0

### Added

* Added support of COPPA

## Version 5.3.2

### Fixed

* Fixed ANR in SDK 5.3.1 during ad loading

## Version 5.3.1

### Added

* Improvements and optimizations

## Version 5.3.0

* Added SDK integration log
* Added getter for banner size
* Improvements and optimizations

## Version 5.2.0

### Breaking changes

* Feedback and domain native ads assets marked as required for all advertising creatives

### Added

* Improvements and optimizations

## Version 5.1.1

### Fixed

* Fixed possible crash with CMP libraries integration

## Version 5.1.0

### Fixed

* Fixed ANR issue

### Added

* Location consent flag
* Added playback events listener in In-Stream
* Improvements and optimizations

### Breaking changes

* Added playback events listener in In-Stream
* Added skip event in In-Stream ad player
* Added playback error reasons in In-Stream ad player

## Version 5.0.0

### Added

* Added performance improvements in In-Stream
* Added callback for ad buffering in In-Stream
* Improvements and optimizations

### Breaking changes

* Changed BlockID to AdUnitID in public API

## Version 5.0.0-beta.1

### Added

* Improvements and optimizations
* Added improvements for fullscreen designs

## Version 5.0.0-alpha.2

### Added

* Improvements and optimizations

#### Fixed

* Fixed bugs

## Version 5.0.0-alpha.1

### Added

* Added Kotlin dependency
* Added AdsLoader API for simplification of In-Stream integration
* Added automatic SDK initialization
* Added precaching for video ads
* Improved ads loading time
* Other improvements and optimizations

#### Fixed

* Fixed bugs

#### Updated

* Major API rework

#### Removed

* Removed deprecated and legacy API

## Version 4.5.0

### Added

* Added com.google.android.gms.permission.AD_ID permission declaration

## Version 4.4.1

### Added

* Added improvements for fullscreen designs
* Added support for initial ad player volume configuration based on video player volume in In-Stream
* Improvements and optimizations

### Updated

* Updated minimum supported AppMetrica SDK version to 4.0.0

## Version 4.4.0

### Added

* Added support for playing multiple video ads sequentially in In-Stream
* Added improvements for fullscreen designs
* Improvements and optimizations

## Version 4.3.0

### Added

* Improvements and optimizations

## Version 4.2.0

### Added

* Optimized rendering of rewarded ads
* Improvements and optimizations

#### Fixed

* Fixed bugs

## Version 4.1.1

#### Added

* Improvements and optimizations

## Version 4.1.0

#### Added

* Improvements and optimizations

## Version 4.0.0

#### Added

* Added new API for request ads
* Added support for a bulk ad request
* Added support for a social ad in In-Stream ads
* Added support for an outdated SDK version indicator
* Improved impression tracking
* Improved requests retry policy
* Updated minimum supported AppMetrica SDK version to 3.18.0
* Other improvements and optimizations

#### Fixed

* Fixed bugs

#### Removed

* Removed deprecated and legacy API

#### Updated

* Major API rework

## Version 3.3.0

#### Added

* Added support for Inrolls
* Added support for Pauserolls
* Improvements and optimizations

#### Fixed

* Fixed feedback displaying in NativeBannerView

## Version 3.2.0

#### Added

* Added method for Mobile Ads SDK initialization
* Added support for In-Stream ads
* Removed dependency from ExoPlayer
* Improvements and optimizations

#### Fixed

* Fixed crashes on wrapper ads loading in VAST
* Fixed cancel of video ads loading

## Version 3.1.1

#### Added

* Improved error indicator
* Improved ads tracking
* Improvements and optimizations

## Version 3.1.0

#### Added

* Added feedback asset to native ads templates
* Improvements and optimizations

#### Fixed

* Fixed displaying of controls during video play in native ads;
* Fixed displaying of the fullscreen ad cross in MIUI Dark Mode;
* Fixed measuring of MediaView size in the native ad template;

## Version 3.0.0

#### Added

* Added onImageAdLoaded callback to NativeAdLoader.OnLoadListener interface
* Added support for native video playback
* Added exoplayer dependency
* Improvements and optimizations

#### Removed

* Removed jar support
* Removed deprecated Vast API
* Removed deprecated constructor in class Tracker
* Removed ability to use ImageView in native ads. Please, use MediaView instead

## Version 2.180

#### Added

* Optimized presenting of fullscreen ads
* Improvements and optimizations

## Version 2.170

#### Added

* Improvements and optimizations

## Version 2.160

#### Added

* Improvements and optimizations

## Version 2.150

#### Added

* Improvements and optimizations

## Version 2.143

#### Added

* Improvements and optimizations

## Version 2.142

#### Added

* Improvements and optimizations

## Version 2.141

#### Added

* Improvements and optimizations

## Version 2.140

#### Added

* Added support for Native Ad Unit
* Improvements and optimizations

## Version 2.130

#### Added

* Added support for sticky banner
* Improvements and optimizations

## Version 2.120

#### Updated

* Improved the speed of loading and displaying native ads

#### Added

* Improvements and optimizations

## Version 2.113

#### Added

* Improvements and optimizations

## Version 2.112

#### Added

* Improvements and optimizations

#### Fixed

* Fixed displaying integration error indicator for native ads

## Version 2.111

#### Updated

* Optimized native ads memory usage

## Version 2.110

#### Added

* Added constructor NativeAdViewBinder$Builder(NativeAdView nativeAdView)

#### Fixed

* Fixed views reuse in native ads

#### Updated

* Improved native view visibility error indicator
* Improvements and optimizations

#### Deprecated

* Deprecated constructor NativeAdViewBinder$Builder(View nativeAdView) in favor of NativeAdViewBinder$Builder(
  NativeAdView nativeAdView)

## Version 2.101

#### Fixed

* Fixed lint error

#### Updated

* Improvements and optimizations

## Version 2.100

#### Added

* Added support for wrappers for VAST ads

#### Updated

* Improvements and optimizations

## Version 2.91

#### Added

* Added support for MediaView in native ad mediation

#### Updated

* Improvements and optimizations

## Version 2.90

#### Added

* Added support for VMAP format

#### Updated

* Improvements and optimizations

#### Deprecated

* Deprecated setOnLoadListener(OnLoadListener) method on NativeAdLoader in favor of setNativeAdLoadListener(
  OnImageAdLoadListener)

## Version 2.81

#### Updated

* Improvements and optimizations

## Version 2.80

#### Added

* Added support for rewarded ad
* Added MediaView and video support in native ad
* Added ability to specify age and gender in AdRequest
* Added VideoEventListener delegate to notify app when ad video completed
* Added support for GDPR to obtain and manage consent from users from GDPR region to serve personalized ads
* Added view visibility error indicator which should help to detect SDK integration problems while testing the
  application

#### Deprecated

* Deprecated setImageView method on NativeAppInstallAdView, NativeContentAdView and NativeImageAdView in favor of
  setMediaView

#### Updated

* Improvements and optimizations

## Version 2.75

#### Updated

* Improvements and optimizations

## Version 2.74

#### Updated

* Improvements and optimizations

## Version 2.73

#### Updated

* Improvements and optimizations

#### Fixed

* Fixed NPE in WebView

## Version 2.72

#### Updated

* Improvements and optimizations

## Version 2.71

#### Added

* Added native ad mediation.

#### Updated

* Improvements and optimizations

## Version 2.70

#### Added

* Automatic activation of the AppMetrica SDK.
* Feedback asset to Native Ads.

#### Updated

* Minor improvements and optimizations

## Version 2.62

#### Fixed

* Fixed VAST loading

#### Updated

* Minor improvements and optimizations

## Version 2.61

#### Added

* Added HTML banners and interstitials mediation

#### Fixed

* Fixed banner ad representation in IDE

#### Updated

* Minor improvements and optimizations

## Version 2.60

#### Updated

* Improved performance for loading ads

#### Fixed

* Fixed Memory Leak

## Version 2.51

#### Updated

* Improved performance for loading ads

## Version 2.50

#### Added

* Added native image ad type

## Version 2.41

#### Updated

* Minor improvements and optimizations

## Version 2.31-adapters

#### Updated

* Update AdMob and MoPub adapters

## Version 2.31

#### Updated

* Minor improvements and optimizations

## Version 2.30

#### Updated

* Minor improvements and optimizations

## Version 2.20

#### Added

* Added ability to get native ad asset values
* Added ability to load native ad images manually
* Added loaded image sizes configuration for native ads

## Version 2.13

#### Updated

* Minor improvements and optimizations

## Version 2.12

#### Updated

* Minor improvements and optimizations

## Version 2.11

#### Updated

* Minor improvements and optimizations

#### Fixed

* Fixed AdMob Adapters

## Version 2.10

#### Added

* Added native ads template view

## Version 2.02

#### Added

* Added native assets highlighting

#### Updated

* Updated minimum compatible AppMetrica version to 2.32
* Minor improvements and optimizations

## Version 2.01

#### Updated

* Minor improvements and optimizations

## Version 2.00

#### Added

* Added `InterstitialEventListener` interface

#### Updated

* Improved SDK behavior in low-memory environment
* Updated javadoc

## Version 2.00-beta-1

#### Added

* Added flexible banner sizes
* Added ability to control SDK logs
* Added ability to get SDK version

#### Updated

* Updated javadoc

#### Breaking changes

* Changed package directory path and name

## Version 1.92

#### Added

* Supported AdMob Adapters
* Supported all banner sizes
* Added custom parameters to AdRequest
* Added demo block ID to banner example

#### Fixed

* Fixed HTML escaped symbols in native ads texts
* Fixed errors

## Version 1.91-mopub

#### Added

* Supported MoPub Adapters

## Version 1.91

#### Added

* Added handling non-protocol links for images in native advertising
* Improved impression tracking

## Version 1.90

#### Added

* Added App Install native ad type
* Added ability to open web links in application or browser

#### Updated

* Updated AppMetrica to 2.00
* Raise minSdkVersion to 10 Android Level.
* Updated javadoc
