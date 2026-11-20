package org.sharad.velvetinvestment.utils

import android.util.Log

actual fun Log(tag: String, log: String) {
    Log.d(tag,log)
}