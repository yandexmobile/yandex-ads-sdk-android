/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2026 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.compose.adunits.playable

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.ads.sample.compose.adunits.playable.ui.PlayablePreviewScreen
import com.yandex.ads.sample.compose.adunits.playable.ui.PlayableQRScannerScreen
import kotlinx.serialization.Serializable

sealed interface PlayableRoute {
    @Serializable
    data object Home : PlayableRoute

    @Serializable
    data object QRScanner : PlayableRoute
}

class PlayablePreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlayableNavigation()
                }
            }
        }
    }
}

@Composable
private fun PlayableNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = PlayableRoute.Home,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300),
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300),
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300),
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300),
            ) + fadeOut(animationSpec = tween(300))
        },
    ) {
        composable<PlayableRoute.Home> {
            PlayablePreviewScreen(
                navController = navController,
                onNavigateBack = { (context as? Activity)?.finish() },
            )
        }
        composable<PlayableRoute.QRScanner> {
            PlayableQRScannerScreen(
                onScanned = { code ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("scanned_code", code)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
