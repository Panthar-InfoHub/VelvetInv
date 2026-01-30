package org.sharad.velvetinvestment.utils

sealed interface WindowSize {
    data object PhonePortrait: WindowSize
    data object PhoneLandscape: WindowSize
    data object Tablet: WindowSize
}