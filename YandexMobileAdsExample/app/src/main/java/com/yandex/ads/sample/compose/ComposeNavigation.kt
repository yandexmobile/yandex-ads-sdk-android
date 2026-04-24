/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.ads.sample.compose.adunits.ComposeBannerInlineScreen
import com.yandex.ads.sample.compose.adunits.ComposeBannerStickyScreen
import com.yandex.ads.sample.compose.adunits.ComposeInterstitialScreen
import com.yandex.ads.sample.compose.adunits.ComposeRewardedScreen
import com.yandex.ads.sample.compose.adunits.ComposeAppOpenAdScreen
import kotlinx.serialization.Serializable

sealed interface ComposeRoute {
    @Serializable
    data object Home : ComposeRoute

    @Serializable
    data object BannerSticky : ComposeRoute

    @Serializable
    data object BannerInline : ComposeRoute

    @Serializable
    data object Interstitial : ComposeRoute

    @Serializable
    data object Rewarded : ComposeRoute

    @Serializable
    data object AppOpenAd : ComposeRoute
}

@Composable
fun ComposeExamplesApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ComposeNavigation()
            }
        }
    }
}

@Composable
fun ComposeNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val navigate: (ComposeRoute) -> Unit = { route ->
        runCatching { navController.navigate(route) }
    }

    NavHost(
        navController = navController,
        startDestination = ComposeRoute.Home,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable<ComposeRoute.Home> {
            ComposeHomeScreen(
                navigate = navigate,
                onNavigateBack = { (context as? Activity)?.finish() }
            )
        }
        composable<ComposeRoute.BannerSticky> {
            ComposeBannerStickyScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable<ComposeRoute.BannerInline> {
            ComposeBannerInlineScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable<ComposeRoute.Interstitial> {
            ComposeInterstitialScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable<ComposeRoute.Rewarded> {
            ComposeRewardedScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable<ComposeRoute.AppOpenAd> {
            ComposeAppOpenAdScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}
