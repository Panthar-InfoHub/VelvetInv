package org.sharad.velvetinvestment.utils

actual fun Log(tag: String, log: String) {
    println("$tag: $log")
}