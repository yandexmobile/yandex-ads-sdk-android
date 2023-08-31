package com.yandex.ads.sample.appopenad

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ProcessLifecycleOwner
import com.yandex.ads.sample.adunits.AppOpenAdActivity
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

class AppOpenAdManager(application: Application) : AppOpenAdLoadListener {

    private val context: Context = application

    private val processLifecycleObserver = DefaultProcessLifecycleObserver(
        onProcessCameForeground = ::showAppOpenAd
    )
    private val appOpenAdActivityObserver = AppOpenAdActivityObserver()

    private val appOpenAdLoader = AppOpenAdLoader(application)
    private val adRequestConfiguration = AdRequestConfiguration.Builder(AD_UNIT_ID).build()
    private var loadingInProgress = AtomicBoolean(false)
    private var activityReference: WeakReference<Activity>? = null
    private var appOpenAd: AppOpenAd? = null

    private val appOpenAdEventListener = AdEventListener()

    init {
        // observe Process lifecycle to choose moment for AppOpenAd show
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
        // observe Activity Callbacks to check if particular activity is started
        application.registerActivityLifecycleCallbacks(appOpenAdActivityObserver)
    }

    fun initialize() {
        appOpenAdLoader.setAdLoadListener(this)
        // load first Ad
        loadAppOpenAd()
    }

    override fun onAdLoaded(appOpenAd: AppOpenAd) {
        // save appOpenAd for future use
        this.appOpenAd = appOpenAd
        loadingInProgress.set(false)
        Toast.makeText(context, "AppOpenAd loaded", Toast.LENGTH_SHORT).show()
    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        loadingInProgress.set(false)
        // use your own reload logic
        // NOTE: avoid continuous reloading when load errors occur
        Toast.makeText(context, "AppOpenAd failed to load", Toast.LENGTH_SHORT).show()
    }

    private fun showAppOpenAd() {
        // show AppOpenAd when Application comes foreground if there is opened specific Activity
        activityReference?.get()
            ?.let(::showAdIfAvailable)
    }

    private fun showAdIfAvailable(activity: Activity) {
        val appOpenAd = appOpenAd
        if (appOpenAd != null) {
            appOpenAd.setAdEventListener(appOpenAdEventListener)
            appOpenAd.show(activity)
        } else {
            loadAppOpenAd()
        }
    }

    private fun loadAppOpenAd() {
        // load new Ad if there is no loaded Ad and new ad isn't loading
        if (loadingInProgress.compareAndSet(false, true)) {
            appOpenAdLoader.loadAd(adRequestConfiguration)
        }
    }

    private fun clearAppOpenAd() {
        appOpenAd?.setAdEventListener(null)
        appOpenAd = null
    }

    private inner class AdEventListener : AppOpenAdEventListener {
        override fun onAdShown() {
            // Called when an app open ad has been shown
        }

        override fun onAdFailedToShow(adError: AdError) {
            // Called when an app open ad failed to show
        }

        override fun onAdDismissed() {
            // Called when an app open ad has been dismissed
            clearAppOpenAd()
            loadAppOpenAd()
        }

        override fun onAdClicked() {
            // Called when user clicked on the ad
        }

        override fun onAdImpression(impressionData: ImpressionData?) {
            // Called when an impression was observed
        }
    }

    private inner class AppOpenAdActivityObserver : DefaultActivityLifecycleCallbacks {

        override fun onActivityStarted(activity: Activity) {
            // example of choosing specific Activity for show AppOpenAd
            activityReference = if (activity is AppOpenAdActivity) {
                WeakReference(activity)
            } else {
                null
            }
        }
    }

    companion object {
        private const val AD_UNIT_ID = "demo-appopenad-yandex"
    }
}
