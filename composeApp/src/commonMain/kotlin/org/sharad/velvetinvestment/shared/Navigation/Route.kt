package org.sharad.velvetinvestment.shared.Navigation

import kotlinx.serialization.Serializable

object Route {

    @Serializable
    data object LoginFlow

    @Serializable
    data object MainAppFlow

    @Serializable
    data object SplashScreen

    @Serializable
    data object LoginScreen

}