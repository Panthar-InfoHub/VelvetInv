package org.sharad.velvetinvestment.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfo
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever

class DeviceInfoRetrieverAndroid(
    private val context: Context
): DeviceInfoRetriever {
    override fun getDeviceInfo(): DeviceInfo {
        val deviceId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"

        return DeviceInfo(
            deviceType = "A",
            deviceVersion = Build.VERSION.RELEASE ?: "unknown",
            deviceBuildNumber = "17",
            deviceId = deviceId
        )
    }
}