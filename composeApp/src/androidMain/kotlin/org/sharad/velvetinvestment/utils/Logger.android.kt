package org.sharad.velvetinvestment.utils

import android.util.Log
import org.sharad.velvetinvestment.GlobalConstants

actual fun Log(tag: String, log: String) {
    if (GlobalConstants.LOGGING){
        Log.d(tag, log)
    }
}