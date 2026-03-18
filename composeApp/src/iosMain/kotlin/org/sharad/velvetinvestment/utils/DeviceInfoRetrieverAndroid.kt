package org.sharad.velvetinvestment.utils


import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfo
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import platform.UIKit.UIDevice

class DeviceInfoRetrieverIos(
): DeviceInfoRetriever {
    override fun getDeviceInfo(): DeviceInfo {
        val device = UIDevice.currentDevice

        return DeviceInfo(
            deviceType = "I",
            deviceVersion = device.systemVersion,
            deviceBuildNumber = device.systemVersion,
            deviceId = device.identifierForVendor?.UUIDString ?: "unknown"
        )
    }
}