package com.yandex.ads.sample.tv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.ads.sample.tv.instream.presentation.InstreamScreen
import com.yandex.ads.sample.tv.splash.SplashScreen
import com.yandex.ads.sample.tv.start.StartScreen

@Composable
fun TvNavigation() {
    val navController = rememberNavController()

    val navigate: (Route) -> Unit = { route ->
        runCatching { navController.navigate(route) }
    }

    NavHost(navController = navController, startDestination = Route.Splash) {
        composable<Route.Splash> {
            SplashScreen(onSplashFinished = {
                navController.navigate(Route.Start) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            })
        }
        composable<Route.Start> {
            StartScreen(navigate = navigate)
        }
        composable<Route.InstreamDemo> {
            InstreamScreen(
                adUnitId = "demo-instream-vmap-yandex",
                onExit = { navController.navigateUp() }
            )
        }
        composable<Route.InstreamPreroll> {
            InstreamScreen(
                adUnitId = "demo-instream-vmap-preroll-yandex",
                onExit = { navController.navigateUp() }
            )
        }
        composable<Route.InstreamMidroll> {
            InstreamScreen(
                adUnitId = "demo-instream-vmap-midroll-yandex",
                onExit = { navController.navigateUp() }
            )
        }
        composable<Route.InstreamPostroll> {
            InstreamScreen(
                adUnitId = "demo-instream-vmap-postroll-yandex",
                onExit = { navController.navigateUp() }
            )
        }
    }
}
