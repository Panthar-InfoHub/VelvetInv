package org.sharad.velvetinvestment.utils

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase

fun String.toCapital(): String {
    return this.toUpperCase(Locale.current)
}