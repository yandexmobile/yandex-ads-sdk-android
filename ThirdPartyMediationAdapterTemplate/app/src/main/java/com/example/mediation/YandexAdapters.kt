package com.example.mediation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.AdType
import com.yandex.mobile.ads.common.BidderTokenLoadListener
import com.yandex.mobile.ads.common.BidderTokenLoader
import com.yandex.mobile.ads.common.BidderTokenRequestConfiguration
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.nativeads.MediaView
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder
import com.yandex.mobile.ads.nativeads.Rating
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

/**
 * This class implements methods for app open ad.
 */
class YandexAppOpenAdapter(
    private val baseAdapter: MediationBaseAdapter = YandexBaseAdapter(),
    private val biddingProvider: MediationBiddingProvider = YandexBiddingProvider(),
    private val adRequestCreator: YandexRequestCreator = YandexRequestCreator()
) : MediationAppOpenAdapter, MediationBiddingProvider {

    /**
     * The loader object is created once and can be reused. This speeds up loading and also allows
     * to implement preloading logic, if the ad network SDK supports it.
     */
    private var appOpenAdLoader: AppOpenAdLoader? = null
    private var appOpenAd: AppOpenAd? = null

    private fun getAppOpenAdLoader(context: Context): AppOpenAdLoader {
        return appOpenAdLoader ?: AppOpenAdLoader(context).also {
            appOpenAdLoader = it
        }
    }

    override fun loadAppOpenAd(
        context: Context,
        params: MediationParameters,
        callback: MediationAppOpenListener
    ) {
        val adLoader = getAppOpenAdLoader(context)

        val loadListener = YandexAppOpenAdListener(callback)
        adLoader.setAdLoadListener(loadListener)

        val adRequest = adRequestCreator.createAdRequestConfiguration(params)
        adLoader.loadAd(adRequest)
    }

    override fun showAppOpenAd(activity: Activity) {
        appOpenAd?.show(activity)
    }

    override fun destroy() {
        appOpenAdLoader?.setAdLoadListener(null)
        appOpenAdLoader = null
        appOpenAd?.setAdEventListener(null)
        appOpenAd = null
    }

    override fun getSDKVersionInfo(): String = baseAdapter.getSDKVersionInfo()

    override fun updatePrivacyPolicies(
        params: MediationParameters
    ) = baseAdapter.updatePrivacyPolicies(params)

    override fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    ) = baseAdapter.initialize(context, params, onInitializationComplete)

    override fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    ) = biddingProvider.loadBidderToken(context, params, callback)

    /**
     * This class represents callback mapper for ad object. Yandex SDK provides separate interfaces
     * for load and events. Most mediation networks provide only one callback object, if ad network
     * provides different listeners this class can be split.
     */
    private inner class YandexAppOpenAdListener(
        private val callback: MediationAppOpenListener
    ) : AppOpenAdLoadListener, AppOpenAdEventListener {

        override fun onAdLoaded(ad: AppOpenAd) {
            appOpenAd = ad.also {
                it.setAdEventListener(this)
            }
            callback.onAdLoaded()
        }

        override fun onAdFailedToLoad(error: AdRequestError) = callback.onAdFailedToLoad()

        override fun onAdShown() = callback.onAdShown()

        override fun onAdFailedToShow(error: AdError) = callback.onAdFailedToShow()

        override fun onAdDismissed() {
            appOpenAd?.setAdEventListener(null)
            appOpenAd = null
            callback.onAdDismissed()
        }

        override fun onAdClicked() = callback.onAdClicked()

        override fun onAdImpression(data: ImpressionData?) = callback.onAdImpression()
    }
}

/**
 * This class implements methods for interstitial ad.
 */
class YandexInterstitialAdapter(
    private val baseAdapter: MediationBaseAdapter = YandexBaseAdapter(),
    private val biddingProvider: MediationBiddingProvider = YandexBiddingProvider(),
    private val adRequestCreator: YandexRequestCreator = YandexRequestCreator()
) : MediationInterstitialAdapter, MediationBiddingProvider {

    /**
     * The loader object is created once and can be reused. This speeds up loading and also allows
     * to implement preloading logic, if the ad network SDK supports it.
     */
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var interstitialAd: InterstitialAd? = null

    private fun getInterstitialAdLoader(context: Context): InterstitialAdLoader {
        return interstitialAdLoader ?: InterstitialAdLoader(context).also {
            interstitialAdLoader = it
        }
    }

    override fun loadInterstitialAd(
        context: Context,
        params: MediationParameters,
        callback: MediationInterstitialListener
    ) {
        val adLoader = getInterstitialAdLoader(context)

        val loadListener = YandexInterstitialAdListener(callback)
        adLoader.setAdLoadListener(loadListener)

        val adRequest = adRequestCreator.createAdRequestConfiguration(params)
        adLoader.loadAd(adRequest)
    }

    override fun showInterstitialAd(activity: Activity) {
        interstitialAd?.show(activity)
    }

    override fun destroy() {
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    override fun getSDKVersionInfo(): String = baseAdapter.getSDKVersionInfo()

    override fun updatePrivacyPolicies(
        params: MediationParameters
    ) = baseAdapter.updatePrivacyPolicies(params)

    override fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    ) = baseAdapter.initialize(context, params, onInitializationComplete)

    override fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    ) = biddingProvider.loadBidderToken(context, params, callback)

    /**
     * This class represents callback mapper for ad object. Yandex SDK provides separate interfaces
     * for load and events. Most mediation networks provide only one callback object, if ad network
     * provides different listeners this class can be split.
     */
    private inner class YandexInterstitialAdListener(
        private val callback: MediationInterstitialListener
    ) : InterstitialAdLoadListener, InterstitialAdEventListener {

        override fun onAdLoaded(ad: InterstitialAd) {
            interstitialAd = ad.also {
                it.setAdEventListener(this)
            }
            callback.onAdLoaded()
        }

        override fun onAdFailedToLoad(error: AdRequestError) = callback.onAdFailedToLoad()

        override fun onAdShown() = callback.onAdShown()

        override fun onAdFailedToShow(error: AdError) = callback.onAdFailedToShow()

        override fun onAdDismissed() {
            interstitialAd?.setAdEventListener(null)
            interstitialAd = null
            callback.onAdDismissed()
        }

        override fun onAdClicked() = callback.onAdClicked()

        override fun onAdImpression(data: ImpressionData?) = callback.onAdImpression()
    }
}

/**
 * This class implements methods for rewarded ad.
 */
class YandexRewardedAdapter(
    private val baseAdapter: MediationBaseAdapter = YandexBaseAdapter(),
    private val biddingProvider: MediationBiddingProvider = YandexBiddingProvider(),
    private val adRequestCreator: YandexRequestCreator = YandexRequestCreator()
) : MediationRewardedAdapter, MediationBiddingProvider {

    /**
     * The loader object is created once and can be reused. This speeds up loading and also allows
     * to implement preloading logic, if the ad network SDK supports it.
     */
    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardedAd: RewardedAd? = null

    private fun getRewardedAdLoader(context: Context): RewardedAdLoader {
        return rewardedAdLoader ?: RewardedAdLoader(context).also {
            rewardedAdLoader = it
        }
    }

    override fun loadRewardedAd(
        context: Context,
        params: MediationParameters,
        callback: MediationRewardedListener
    ) {
        val adLoader = getRewardedAdLoader(context)

        val loadListener = YandexRewardedAdListener(callback)
        adLoader.setAdLoadListener(loadListener)

        val adRequest = adRequestCreator.createAdRequestConfiguration(params)
        adLoader.loadAd(adRequest)
    }

    override fun showRewardedAd(activity: Activity) {
        rewardedAd?.show(activity)
    }

    override fun destroy() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

    override fun getSDKVersionInfo(): String = baseAdapter.getSDKVersionInfo()

    override fun updatePrivacyPolicies(
        params: MediationParameters
    ) = baseAdapter.updatePrivacyPolicies(params)

    override fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    ) = baseAdapter.initialize(context, params, onInitializationComplete)

    override fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    ) = biddingProvider.loadBidderToken(context, params, callback)

    /**
     * This class represents callback mapper for ad object. Yandex SDK provides separate interfaces
     * for load and events. Most mediation networks provide only one callback object, if ad network
     * provides different listeners this class can be split.
     */
    private inner class YandexRewardedAdListener(
        private val callback: MediationRewardedListener
    ) : RewardedAdLoadListener, RewardedAdEventListener {

        override fun onAdLoaded(ad: RewardedAd) {
            rewardedAd = ad.also {
                it.setAdEventListener(this)
            }
            callback.onAdLoaded()
        }

        override fun onAdFailedToLoad(error: AdRequestError) = callback.onAdFailedToLoad()

        override fun onAdShown() = callback.onAdShown()

        override fun onAdFailedToShow(error: AdError) = callback.onAdFailedToShow()

        override fun onAdDismissed() {
            rewardedAd?.setAdEventListener(null)
            rewardedAd = null
            callback.onAdDismissed()
        }

        override fun onAdClicked() = callback.onAdClicked()

        override fun onAdImpression(data: ImpressionData?) = callback.onAdImpression()

        override fun onRewarded(reward: Reward) = callback.onRewarded()
    }
}

/**
 * This class implements methods for banner ad.
 */
class YandexBannerAdapter(
    private val baseAdapter: MediationBaseAdapter = YandexBaseAdapter(),
    private val biddingProvider: MediationBiddingProvider = YandexBiddingProvider(),
    private val adRequestCreator: YandexRequestCreator = YandexRequestCreator(),
    private val parametersMapper: YandexParametersMapper = YandexParametersMapper()
) : MediationBannerAdapter, MediationBiddingProvider {

    override fun loadBannerAd(
        context: Context,
        params: MediationParameters,
        callback: MediationBannerListener
    ) {
        val adFormat = params.getAdFormat()
        if (adFormat !is MediationAdFormat.Banner) {
            Log.e("YandexBannerAdapter", "Expected Banner ad format, but actual $adFormat")
            return
        }

        val bannerAd = BannerAdView(context)
        val eventListener = YandexBannerAdListener(bannerAd, callback)

        with(bannerAd) {
            setAdSize(parametersMapper.getBannerAdSize(context, adFormat))
            setAdUnitId(params.getAdUnitId())
            setBannerAdEventListener(eventListener)

            loadAd(adRequestCreator.createAdRequest(params))
        }
    }

    override fun getSDKVersionInfo(): String = baseAdapter.getSDKVersionInfo()

    override fun updatePrivacyPolicies(
        params: MediationParameters
    ) = baseAdapter.updatePrivacyPolicies(params)

    override fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    ) = baseAdapter.initialize(context, params, onInitializationComplete)

    override fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    ) = biddingProvider.loadBidderToken(context, params, callback)

    /**
     * This class represents callback mapper for ad object.
     */
    private class YandexBannerAdListener(
        private val bannerAdView: BannerAdView,
        private val callback: MediationBannerListener
    ) : BannerAdEventListener {

        override fun onAdLoaded() = callback.onAdLoaded(bannerAdView)

        override fun onAdFailedToLoad(error: AdRequestError) = callback.onAdFailedToLoad()

        override fun onAdClicked() = callback.onAdClicked()

        override fun onLeftApplication() = callback.onLeftApplication()

        override fun onReturnedToApplication() = callback.onReturnedToApplication()

        override fun onImpression(data: ImpressionData?) = callback.onImpression()
    }
}

/**
 * This class implements methods for native ad.
 */
class YandexNativeAdapter(
    private val baseAdapter: MediationBaseAdapter = YandexBaseAdapter(),
    private val biddingProvider: MediationBiddingProvider = YandexBiddingProvider(),
    private val adRequestCreator: YandexRequestCreator = YandexRequestCreator(),
    private val mapperCreator: YandexNativeMapperCreator = YandexNativeMapperCreator()
) : MediationNativeAdapter, MediationBiddingProvider {

    /**
     * The loader object is created once and can be reused. This speeds up loading and also allows
     * to implement preloading logic, if the ad network SDK supports it.
     */
    private var nativeAdLoader: NativeAdLoader? = null

    private fun getNativeAdLoader(context: Context): NativeAdLoader {
        return nativeAdLoader ?: NativeAdLoader(context).also {
            nativeAdLoader = it
        }
    }

    override fun loadNativeAd(
        context: Context,
        params: MediationParameters,
        callback: MediationNativeListener
    ) {
        val adLoader = getNativeAdLoader(context)

        val loadListener = YandexNativeAdListener(context, params.getExtraViewsIds(), callback)
        adLoader.setNativeAdLoadListener(loadListener)

        val adRequest = adRequestCreator.createNativeAdRequestConfiguration(params)
        adLoader.loadAd(adRequest)
    }

    override fun destroy() {
        nativeAdLoader?.setNativeAdLoadListener(null)
        nativeAdLoader = null
    }

    override fun getSDKVersionInfo(): String = baseAdapter.getSDKVersionInfo()

    override fun updatePrivacyPolicies(
        params: MediationParameters
    ) = baseAdapter.updatePrivacyPolicies(params)

    override fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    ) = baseAdapter.initialize(context, params, onInitializationComplete)

    override fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    ) = biddingProvider.loadBidderToken(context, params, callback)

    /**
     * This class represents callback mapper for ad object. Yandex SDK provides separate interfaces
     * for load and events. Most mediation networks provide only one callback object, if ad network
     * provides different listeners this class can be split.
     */
    private inner class YandexNativeAdListener(
        private val context: Context,
        private val extraViewIds: Map<String, Int>,
        private val callback: MediationNativeListener
    ) : NativeAdLoadListener, NativeAdEventListener {

        override fun onAdLoaded(ad: NativeAd) {
            ad.setNativeAdEventListener(this)
            val mapper = mapperCreator.create(context, ad, extraViewIds)
            callback.onAdLoaded(mapper)
        }

        override fun onAdFailedToLoad(error: AdRequestError) = callback.onAdFailedToLoad()

        override fun onAdClicked() = callback.onAdClicked()

        override fun onLeftApplication() = callback.onLeftApplication()

        override fun onReturnedToApplication() = callback.onReturnedToApplication()

        override fun onImpression(data: ImpressionData?) = callback.onImpression()
    }
}

/**
 * This class implements the base logic of adapter.
 */
class YandexBaseAdapter: MediationBaseAdapter {

    /**
     * Yandex SDK version can be obtained that way.
     */
    override fun getSDKVersionInfo(): String = MobileAds.libraryVersion

    /**
     * Method for updating privacy policies.
     *
     * @see <a href="https://yandex.ru/support2/mobile-ads/en/dev/android/gdpr">
     *     GDPR</a>,
     *     <a href="https://yandex.ru/support2/mobile-ads/en/dev/android/coppa">
     *     COPPA</a>
     */
    override fun updatePrivacyPolicies(params: MediationParameters) {
        MobileAds.setUserConsent(params.hasUserConsent())
        MobileAds.setAgeRestrictedUser(params.isAgeRestrictedUser())
        MobileAds.setLocationConsent(params.hasLocationConsent())
    }

    /**
     * Method for manual SDK initialization. By default, the SDK is initialized automatically at app
     * startup. That accelerates ad loading and consequently increases monetization revenue.
     *
     * @see <a href=
     *     "https://yandex.ru/support2/mobile-ads/en/dev/android/quick-start#init">
     *     Initialization guide</a>
     */
    override fun initialize(
        context: Context,
        params: MediationParameters,
        onInitializationComplete: () -> Unit
    ) {
        if (params.isTesting()) {
            MobileAds.enableLogging(true)
        }

        MobileAds.initialize(context) {
            onInitializationComplete.invoke()
        }
    }
}

/**
 * This class is used for mapping ad network parameters to Yandex parameters.
 */
class YandexParametersMapper {

    fun getBannerAdSize(context: Context, adNetworkBanner: MediationAdFormat.Banner): BannerAdSize {
        return when (adNetworkBanner.type) {
            MediationBannerType.Banner -> BannerAdSize.fixedSize(context, 320, 50)
            MediationBannerType.Leader -> BannerAdSize.fixedSize(context, 728, 90)
            MediationBannerType.Mrec -> BannerAdSize.fixedSize(context, 300, 250)
            is MediationBannerType.Custom -> {
                BannerAdSize.fixedSize(
                    context,
                    adNetworkBanner.type.width,
                    adNetworkBanner.type.height
                )
            }
        }
    }

    fun getAdType(params: MediationParameters): AdType {
        return when (params.getAdFormat()) {
            MediationAdFormat.Interstitial -> AdType.INTERSTITIAL
            MediationAdFormat.Rewarded -> AdType.REWARDED
            MediationAdFormat.AppOpen -> AdType.APP_OPEN_AD
            MediationAdFormat.Native -> AdType.NATIVE
            is MediationAdFormat.Banner -> AdType.BANNER
        }
    }
}

/**
 * This class is used for creating different request to Yandex SDK.
 */
class YandexRequestCreator(
    private val parametersMapper: YandexParametersMapper = YandexParametersMapper()
) {

    fun createAdRequest(
        params: MediationParameters
    ) = AdRequest.Builder()
        .setBiddingData(params.getBidderToken())
        .setParameters(extractMediationParameters(params))
        .build()

    fun createAdRequestConfiguration(
        params: MediationParameters
    ) = AdRequestConfiguration.Builder(params.getAdUnitId())
        .setBiddingData(params.getBidderToken())
        .setParameters(extractMediationParameters(params))
        .build()

    fun createNativeAdRequestConfiguration(
        params: MediationParameters
    ) = NativeAdRequestConfiguration.Builder(params.getAdUnitId())
        .setBiddingData(params.getBidderToken())
        .setShouldLoadImagesAutomatically(true)
        .setParameters(extractMediationParameters(params))
        .build()

    fun createBidderTokenRequestConfiguration(
        context: Context,
        params: MediationParameters
    ): BidderTokenRequestConfiguration {
        val adType = parametersMapper.getAdType(params)
        val requestBuilder = BidderTokenRequestConfiguration.Builder(adType)

        val adFormat = params.getAdFormat()
        if (adFormat is MediationAdFormat.Banner) {
            requestBuilder.setBannerAdSize(parametersMapper.getBannerAdSize(context, adFormat))
        }

        return requestBuilder
            .setParameters(extractMediationParameters(params))
            .build()
    }

    /**
     * Method for obtaining necessary mediation parameters for requests.
     */
    private fun extractMediationParameters(params: MediationParameters) = mapOf(
        ADAPTER_VERSION_KEY to params.getAdapterVersion(),
        ADAPTER_NETWORK_NAME_KEY to params.getAdapterNetworkName(),
        ADAPTER_NETWORK_SDK_VERSION_KEY to params.getAdapterNetworkSdkVersion(),
    )

    companion object {

        private const val ADAPTER_VERSION_KEY = "adapter_version"
        private const val ADAPTER_NETWORK_NAME_KEY = "adapter_network_name"
        private const val ADAPTER_NETWORK_SDK_VERSION_KEY = "adapter_network_sdk_version"
    }
}

/**
 * This class implements methods for bidding token generation.
 */
class YandexBiddingProvider(
    private val adRequestCreator: YandexRequestCreator = YandexRequestCreator()
) : MediationBiddingProvider {

    /**
     * This method are used to obtain a bidding token. If the ad network supports s2s bidding, token
     * can be obtained that way.
     */
    override fun loadBidderToken(
        context: Context,
        params: MediationParameters,
        callback: MediationBiddingListener
    ) {
        val tokenRequest = adRequestCreator.createBidderTokenRequestConfiguration(context, params)
        val loadListener = YandexBidderTokenListener(callback)

        BidderTokenLoader.loadBidderToken(context, tokenRequest, loadListener)
    }

    /**
     * This class represents callback mapper for ad object.
     */
    private class YandexBidderTokenListener(
        private val callback: MediationBiddingListener
    ) : BidderTokenLoadListener {

        override fun onBidderTokenLoaded(token: String) = callback.onTokenLoaded(token)

        override fun onBidderTokenFailedToLoad(msg: String) = callback.onTokenFailedToLoad()
    }
}

/**
 * This class implements a native ad object that the ad network SDK expects after it is loaded.
 * [YandexNativeMapperCreator] creates this object and binds native assets to it.
 *
 * @param nativeAd object of native ad received from Yandex SDK.
 * @param extraViewIds map that matches some names of components with their IDs in the
 *        publisher layout.
 */
class YandexNativeMapper(
    private val nativeAd: NativeAd,
    private val extraViewIds: Map<String, Int>
) : MediationNativeMapper() {

    /**
     * Method for properly binding views in native ad format. In the real case this logic is
     * strongly depends on the ad network SDK and should be changed to match it.
     */
    override fun trackViews(adNetworkView: MediationNativeAdView) {
        val mainView = adNetworkView.getMainView()
        adNetworkView.removeView(mainView)

        val nativeAdView = NativeAdView(adNetworkView.context).apply { addView(mainView) }
        adNetworkView.addView(nativeAdView)

        val binder = createBinder(adNetworkView, nativeAdView)
        nativeAd.bindNativeAd(binder)
    }

    /**
     * Method for creating binder with native ad components.
     */
    private fun createBinder(
        adNetworkView: MediationNativeAdView,
        nativeAdView: NativeAdView
    ) = NativeAdViewBinder.Builder(nativeAdView)
        .setAgeView(adNetworkView.ageTextView)
        .setBodyView(adNetworkView.bodyTextView)
        .setCallToActionView(adNetworkView.ctaTextView)
        .setDomainView(adNetworkView.domainTextView)
        .setFaviconView(adNetworkView.faviconImageView)
        .setFeedbackView(adNetworkView.getFeedbackView())
        .setIconView(adNetworkView.iconImageView)
        .setMediaView(adNetworkView.getMediaView())
        .setPriceView(adNetworkView.priceTextView)
        .setReviewCountView(adNetworkView.reviewCountTextView)
        .setSponsoredView(adNetworkView.sponsoredTextView)
        .setTitleView(adNetworkView.titleTextView)
        .setWarningView(adNetworkView.warningTextView)
        .apply {
            val ratingView = adNetworkView.getRatingView()
            if (ratingView is Rating) {
                setRatingView(ratingView)
            }
        }.build()

    private fun MediationNativeAdView.getMediaView(): MediaView? {
        return mediaView as? MediaView
    }

    private fun MediationNativeAdView.getFeedbackView(): ImageView? {
        return extraViewIds[FEEDBACK]?.let { findViewById<View>(it) } as? ImageView
    }

    private fun MediationNativeAdView.getRatingView(): View? {
        return extraViewIds[RATING]?.let { findViewById(it) }
    }

    /**
     * Example of tags for native ad view providing.
     * Publisher should provide view IDs with these tags such that the
     * [com.example.mediation.MediationParameters.getExtraViewsIds] method returns them.
     */
    companion object YandexNativeAdAsset {

        const val FEEDBACK = "feedback"
        const val RATING = "rating"
    }
}

/**
 * This class is wrapper for [YandexNativeMapper] creating.
 */
class YandexNativeMapperCreator {

    /**
     * Method for creating [YandexNativeMapper] and binding native assets to it.
     */
    fun create(
        context: Context,
        nativeAd: NativeAd,
        extraViewIds: Map<String, Int>
    ): YandexNativeMapper {
        val assets = nativeAd.adAssets
        return YandexNativeMapper(nativeAd, extraViewIds).apply {
            age = assets.age
            body = assets.body
            callToAction = assets.callToAction
            domain = assets.domain
            favicon = assets.favicon?.bitmap
            icon = assets.icon?.bitmap
            media = MediaView(context)
            price = assets.price
            starRating = assets.rating
            reviewCount = assets.reviewCount
            sponsored = assets.sponsored
            title = assets.title
            warning = assets.warning
        }
    }
}
