package com.example.mediation

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * This group of classes represents different types of ad in the ad network SDK.
 *
 * [Interstitial] represents Interstitial ad
 * [Rewarded] represents Rewarded ad
 * [AppOpen] represents AppOpen ad
 * [Native] represents Native ad
 * [Banner] represents Banner ad
 */
sealed interface MediationAdFormat {
    data object Interstitial : MediationAdFormat
    data object Rewarded : MediationAdFormat
    data object AppOpen : MediationAdFormat
    data object Native : MediationAdFormat
    data class Banner(val type: MediationBannerType) : MediationAdFormat
}

/**
 * This interface describes parameters of the ad network SDK which are passed to the adapter in
 * order to work with ads. In the real case these parameters can be obtained in other ways based on
 * ad network SDK.
 */
interface MediationParameters {
    /**
     * @return requested ad unit id.
     */
    fun getAdUnitId(): String

    /**
     * @return ad network format.
     */
    fun getAdFormat(): MediationAdFormat

    /**
     * @return bidder token.
     */
    fun getBidderToken(): String

    /**
     * Method for obtaining extra views for native ad format.
     *
     * @return Map that matches some names of components with their IDs in the publisher layout.
     *
     * @see <a href=
     *     "https://yandex.ru/support2/mobile-ads/en/dev/android/components">
     *     Yandex native ad assets required for impression</a>
     */
    fun getExtraViewsIds(): Map<String, Int>

    /**
     * @return has user consent.
     */
    fun hasUserConsent(): Boolean

    /**
     * @return is age restricted.
     */
    fun isAgeRestrictedUser(): Boolean

    /**
     * @return is location processing enabled.
     */
    fun hasLocationConsent(): Boolean

    /**
     * @return mediation adapter version.
     */
    fun getAdapterVersion(): String

    /**
     * @return lowercase ad network name.
     */
    fun getAdapterNetworkName(): String

    /**
     * @return ad network SDK version.
     */
    fun getAdapterNetworkSdkVersion(): String

    /**
     * @return is testing enabled.
     */
    fun isTesting(): Boolean
}

/**
 * This interface describes the base logic of adapter.
 *
 * @see com.example.mediation.YandexBaseAdapter
 */
interface MediationBaseAdapter {
    /**
     * Method for obtaining SDK version.
     */
    fun getSDKVersionInfo(): String

    /**
     * Method for updating privacy policies like GDPR, COPPA and location processing.
     *
     * @param params Parameters for obtaining policy states from ad network SDK.
     */
    fun updatePrivacyPolicies(params: MediationParameters)

    /**
     * Method for manual SDK initialization.
     *
     * @param context
     * @param params Parameters for SDK initialization.
     * @param onInitializationComplete Callback for successful initialization event.
     */
    fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    )
}

/**
 * This interface describes listener for bidding token generation events.
 */
interface MediationBiddingListener {
    fun onTokenLoaded(token: String)
    fun onTokenFailedToLoad()
}

/**
 * This interface describes methods for bidding token generation.
 *
 * @see com.example.mediation.YandexBiddingProvider
 */
interface MediationBiddingProvider {
    /**
     * Method for load a bidding token.
     *
     * @param context
     * @param params Parameters for token request configuration.
     * @param callback Listener that implements network ad callbacks.
     */
    fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    )
}

/**
 * This interface describes listener for app open ad events.
 */
interface MediationAppOpenListener {
    fun onAdLoaded()
    fun onAdFailedToLoad()
    fun onAdShown()
    fun onAdFailedToShow()
    fun onAdDismissed()
    fun onAdClicked()
    fun onAdImpression()
}

/**
 * This interface describes methods for app open ad.
 *
 * @see com.example.mediation.YandexAppOpenAdapter
 */
interface MediationAppOpenAdapter : MediationBaseAdapter {
    /**
     * Method for load an ad object.
     *
     * @param context
     * @param params Parameters for ads request configuration.
     * @param callback Listener that implements network ad callbacks.
     */
    fun loadAppOpenAd(
        context: Context,
        params: MediationParameters,
        callback: MediationAppOpenListener
    )

    /**
     * Method for show an ad object.
     */
    fun showAppOpenAd(activity: Activity)

    /**
     * A destroy method for ad instances.
     */
    fun destroy()
}

/**
 * This interface describes a listener for interstitial ad events.
 */
interface MediationInterstitialListener {
    fun onAdLoaded()
    fun onAdFailedToLoad()
    fun onAdShown()
    fun onAdFailedToShow()
    fun onAdDismissed()
    fun onAdClicked()
    fun onAdImpression()
}

/**
 * This interface describes methods for interstitial ad.
 *
 * @see com.example.mediation.YandexInterstitialAdapter
 */
interface MediationInterstitialAdapter : MediationBaseAdapter {
    /**
     * Method for load an ad object.
     *
     * @param context
     * @param params Parameters for ads request configuration.
     * @param callback Listener that implements network ad callbacks.
     */
    fun loadInterstitialAd(
        context: Context,
        params: MediationParameters,
        callback: MediationInterstitialListener
    )

    /**
     * Method for show an ad object.
     */
    fun showInterstitialAd(activity: Activity)

    /**
     * A destroy method for ad instances.
     */
    fun destroy()
}

/**
 * This interface describes a listener for rewarded ad events.
 */
interface MediationRewardedListener {
    fun onAdLoaded()
    fun onAdFailedToLoad()
    fun onAdShown()
    fun onAdFailedToShow()
    fun onAdDismissed()
    fun onAdClicked()
    fun onAdImpression()
    fun onRewarded()
}

/**
 * This interface describes methods for rewarded ad.
 *
 * @see com.example.mediation.YandexRewardedAdapter
 */
interface MediationRewardedAdapter : MediationBaseAdapter {
    /**
     * Method for load an ad object.
     *
     * @param context
     * @param params Parameters for ads request configuration.
     * @param callback Listener that implements network ad callbacks.
     */
    fun loadRewardedAd(
        context: Context,
        params: MediationParameters,
        callback: MediationRewardedListener
    )

    /**
     * Method for show an ad object.
     */
    fun showRewardedAd(activity: Activity)

    /**
     * A destroy method for ad instances.
     */
    fun destroy()
}

/**
 * This group of classes represents different types of banner ad in the ad network SDK. In general,
 * these classes are used for banner size mapping in the adapter. If ad network SDK has a simpler
 * way to get width and height, adapter should use it.
 *
 * [Banner] represents 320x50 banner.
 * [Leader] represents 728x90 banner.
 * [Mrec] represents 300x250 banner.
 * [Custom] represents custom banner.
 */
sealed interface MediationBannerType {
    data object Banner : MediationBannerType
    data object Leader : MediationBannerType
    data object Mrec : MediationBannerType
    data class Custom(val width: Int, val height: Int) : MediationBannerType
}

/**
 * This interface describes listener for banner ad events.
 */
interface MediationBannerListener {
    fun onAdLoaded(loadedBanner: View)
    fun onAdFailedToLoad()
    fun onAdClicked()
    fun onLeftApplication()
    fun onReturnedToApplication()
    fun onImpression()
}

/**
 * This interface describes methods for banner ad.
 *
 * @see com.example.mediation.YandexBannerAdapter
 */
interface MediationBannerAdapter : MediationBaseAdapter {
    /**
     * Method for load an ad object.
     *
     * @param context
     * @param params Parameters for ads request configuration.
     * @param callback Listener that implements network ad callbacks.
     */
    fun loadBannerAd(
        context: Context,
        params: MediationParameters,
        callback: MediationBannerListener
    )
}

/**
 * This class represents a native ad view of the ad network which will be used to obtain ad assets.
 * Ad network required views and Yandex required views may mismatch. Check docs to resolve it.
 *
 * @see com.example.mediation.YandexNativeMapper.trackViews
 * @see <a href=
 *     "https://yandex.ru/support2/mobile-ads/en/dev/android/components">
 *     Yandex native ad assets required for impression</a>
 */
abstract class MediationNativeAdView(context: Context) : ViewGroup(context) {
    abstract val ageTextView: TextView
    abstract val bodyTextView: TextView
    abstract val ctaTextView: TextView
    abstract val domainTextView: TextView
    abstract val faviconImageView: ImageView
    abstract val iconImageView: ImageView
    abstract val mediaView: View
    abstract val priceTextView: TextView
    abstract val reviewCountTextView: TextView
    abstract val sponsoredTextView: TextView
    abstract val titleTextView: TextView
    abstract val warningTextView: TextView

    abstract fun getMainView(): View
}


/**
 * This class represents a native ad object that the ad network SDK expects after it is loaded. The
 * adapter will extend and bind this class after successful ad loading. In the real case this logic
 * is strongly depends on the ad network SDK and should be changed to match it.
 *
 * @see com.example.mediation.YandexNativeMapper
 */
abstract class MediationNativeMapper {

    /**
     * Method for properly binding views in native ad format.
     */
    abstract fun trackViews(adNetworkView: MediationNativeAdView)

    var age: String? = null
    var body: String? = null
    var callToAction: String? = null
    var domain: String? = null
    var favicon: Bitmap? = null
    var icon: Bitmap? = null
    var media: View? = null
    var price: String? = null
    var starRating: Float? = null
    var reviewCount: String? = null
    var sponsored: String? = null
    var title: String? = null
    var warning: String? = null
}

/**
 * This interface describes a listener for native ad events.
 */
interface MediationNativeListener {
    fun onAdLoaded(mapper: MediationNativeMapper)
    fun onAdFailedToLoad()
    fun onAdClicked()
    fun onLeftApplication()
    fun onReturnedToApplication()
    fun onImpression()
}

/**
 * This interface describes methods for native ad.
 *
 * @see com.example.mediation.YandexNativeAdapter
 */
interface MediationNativeAdapter : MediationBaseAdapter {
    /**
     * Method for load an ad object.
     *
     * @param context
     * @param params Parameters for ads request configuration.
     * @param callback Listener that implements network ad callbacks.
     */
    fun loadNativeAd(
        context: Context,
        params: MediationParameters,
        callback: MediationNativeListener
    )

    /**
     * A destroy method for ad instances.
     */
    fun destroy()
}
