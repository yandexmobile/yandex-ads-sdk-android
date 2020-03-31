# Change Log
All notable changes to Yandex Mobile Ads SDK will be documented in this file.

## Version 2.140

#### Added
* Added support for Native Ad Unit
* Improvements and optimizations

## Version 2.130

#### Added
* Added support for sticky banner
* Improvements and optimizations

## Version 2.120

#### Updated
* Improved the speed of loading and displaying native ads

#### Added
* Improvements and optimizations

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
* Deprecated constructor NativeAdViewBinder$Builder(View nativeAdView) in favor of NativeAdViewBinder$Builder(NativeAdView nativeAdView)

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
* Deprecated setOnLoadListener(OnLoadListener) method on NativeAdLoader in favor of setNativeAdLoadListener(OnImageAdLoadListener)

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
* Added view visibility error indicator which should help to detect SDK integration problems while testing the application

#### Deprecated
* Deprecated setImageView method on NativeAppInstallAdView, NativeContentAdView and NativeImageAdView in favor of setMediaView

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

#### Breaking changes
* Changed AdUnitID to BlockID in public API