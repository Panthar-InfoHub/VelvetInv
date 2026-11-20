package org.sharad.velvetinvestment.utils

import org.sharad.velvetinvestment.GlobalConstants

actual fun Log(tag: String, log: String) {
    if (GlobalConstants.LOGGING) {
        println("$tag: $log")
    }
}