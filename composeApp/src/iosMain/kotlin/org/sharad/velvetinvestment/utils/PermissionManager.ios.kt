package org.sharad.velvetinvestment.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return PermissionsManager(callback)
}

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) : PermissionHandler {
    @Composable
    actual override fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.GALLERY -> {
                val status = remember { PHPhotoLibrary.authorizationStatus() }
                askGalleryPermission(status, permission, callback)
            }
            PermissionType.DOCUMENT -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
        }
    }

    private fun askGalleryPermission(status: PHAuthorizationStatus, permission: PermissionType, callback: PermissionCallback) {
        when (status) {
            PHAuthorizationStatusAuthorized -> callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            PHAuthorizationStatusNotDetermined -> PHPhotoLibrary.requestAuthorization { newStatus ->
                askGalleryPermission(newStatus, permission, callback)
            }
            PHAuthorizationStatusDenied -> callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            else -> error("Unknown gallery status $status")
        }
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.GALLERY -> PHPhotoLibrary.authorizationStatus() == PHAuthorizationStatusAuthorized
            PermissionType.DOCUMENT -> true
        }
    }

    @Composable
    actual override fun launchSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        UIApplication.sharedApplication.openURL(url!!)
    }
}