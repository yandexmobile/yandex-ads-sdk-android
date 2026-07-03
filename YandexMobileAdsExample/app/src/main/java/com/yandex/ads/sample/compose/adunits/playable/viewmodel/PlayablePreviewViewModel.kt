/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2026 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose.adunits.playable.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.yandex.ads.sample.compose.adunits.playable.model.PlayableItem
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class PlayablePreviewViewModel(application: Application) : AndroidViewModel(application) {

    sealed interface AdState {
        data object Idle : AdState
        data object Loading : AdState
        data object ReadyToPresent : AdState
        data class Error(val message: String) : AdState
    }

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _history = MutableStateFlow<List<PlayableItem>>(emptyList())
    val history: StateFlow<List<PlayableItem>> = _history.asStateFlow()

    private val _selectedItem = MutableStateFlow<PlayableItem?>(null)
    val selectedItem: StateFlow<PlayableItem?> = _selectedItem.asStateFlow()

    private val _withVideo = MutableStateFlow(false)
    val withVideo: StateFlow<Boolean> = _withVideo.asStateFlow()

    private val _adState = MutableStateFlow<AdState>(AdState.Idle)
    val adState: StateFlow<AdState> = _adState.asStateFlow()

    private val _interstitialAd = MutableStateFlow<InterstitialAd?>(null)
    internal val interstitialAd: StateFlow<InterstitialAd?> = _interstitialAd.asStateFlow()

    private var adLoader: InterstitialAdLoader? = null

    init {
        loadHistory()
    }

    fun addItem(scannedCode: String) {
        val id = extractPlayableId(scannedCode) ?: return
        val existing = _history.value.find { it.id == id }
        if (existing != null) {
            selectItem(existing)
            return
        }
        val item = PlayableItem(id = id)
        _history.value = listOf(item) + _history.value
        _selectedItem.value = item
        saveHistory()
        load()
    }

    fun selectItem(item: PlayableItem) {
        if (_selectedItem.value?.id == item.id) {
            val state = _adState.value
            if (state is AdState.Loading || state is AdState.ReadyToPresent) return
        } else {
            _selectedItem.value = item
        }
        _adState.value = AdState.Idle
        _interstitialAd.value = null
        load()
    }

    fun deleteItem(id: String) {
        if (_selectedItem.value?.id == id) {
            adLoader?.cancelLoading()
            _selectedItem.value = null
            _adState.value = AdState.Idle
            _interstitialAd.value = null
        }
        _history.value = _history.value.filter { it.id != id }
        saveHistory()
    }

    fun toggleWithVideo() {
        _withVideo.value = _withVideo.value.not()
    }

    private fun load() {
        val item = _selectedItem.value ?: return
        _adState.value = AdState.Loading
        _interstitialAd.value = null

        adLoader?.cancelLoading()
        val adUnitId = if (_withVideo.value) AD_UNIT_ID_VIDEO else AD_UNIT_ID
        adLoader = InterstitialAdLoader(getApplication<Application>()).also { loader ->
            loader.loadAd(
                AdRequest.Builder(adUnitId).setParameters(mapOf("playable_url" to item.id)).build(),
                object : InterstitialAdLoadListener {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        _interstitialAd.value = ad
                        _adState.value = AdState.ReadyToPresent
                    }

                    override fun onAdFailedToLoad(error: AdRequestError) {
                        _adState.value = AdState.Error(error.description)
                    }
                }
            )
        }
    }

    fun showAd(activity: Activity) {
        val ad = interstitialAd.value ?: return
        ad.setAdEventListener(null)
        ad.setAdEventListener(object : InterstitialAdEventListener {
            override fun onAdShown() {}

            override fun onAdFailedToShow(error: AdError) {
                ad.setAdEventListener(null)
                _interstitialAd.value = null
                _adState.value = AdState.Idle
            }

            override fun onAdDismissed() {
                ad.setAdEventListener(null)
                _interstitialAd.value = null
                _adState.value = AdState.Idle
            }

            override fun onAdClicked() {}

            override fun onAdImpression(data: ImpressionData?) {}
        })
        ad.show(activity)
    }

    internal fun extractPlayableId(code: String): String? {
        if (isValidUuid(code)) return code
        val lastSegment = code.trimEnd('/').substringAfterLast('/')
        if (isValidUuid(lastSegment)) return lastSegment
        return null
    }

    private fun isValidUuid(s: String): Boolean = try {
        UUID.fromString(s)
        true
    } catch (e: IllegalArgumentException) {
        false
    }

    private fun saveHistory() {
        prefs.edit().putString(HISTORY_KEY, json.encodeToString(_history.value)).apply()
    }

    private fun loadHistory() {
        val jsonString = prefs.getString(HISTORY_KEY, null) ?: return
        _history.value = runCatching {
            json.decodeFromString<List<PlayableItem>>(jsonString)
        }.getOrElse { emptyList() }
    }

    override fun onCleared() {
        adLoader?.cancelLoading()
        _interstitialAd.value?.setAdEventListener(null)
        super.onCleared()
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        private const val PREFS_NAME = "playable_preview_prefs"
        private const val HISTORY_KEY = "playable_preview_history"
        private const val AD_UNIT_ID = "demo-interstitial-playable"
        private const val AD_UNIT_ID_VIDEO = "demo-interstitial-playable-video"
    }
}
