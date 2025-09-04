package com.yandex.ads.sample.tv.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object Splash : Route

    @Serializable
    object Start : Route

    @Serializable
    object InstreamDemo : Route

    @Serializable
    object InstreamPreroll : Route

    @Serializable
    object InstreamMidroll : Route

    @Serializable
    object InstreamPostroll : Route
}
