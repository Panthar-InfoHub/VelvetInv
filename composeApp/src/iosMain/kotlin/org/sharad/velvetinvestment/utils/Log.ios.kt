package org.sharad.velvetinvestment.utils

import platform.Foundation.NSLog

actual fun Log(tag: String, log: String) {
    NSLog("[$tag] $log")
}