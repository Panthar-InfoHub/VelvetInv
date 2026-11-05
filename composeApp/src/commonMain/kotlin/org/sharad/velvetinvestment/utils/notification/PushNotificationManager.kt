package org.sharad.velvetinvestment.utils.notification

/**
 * Identifies this app install to the backend for push (JN). Sent as `fcm_token` at login, but it
 * is the **Firebase Installation ID**, not an FCM registration token: FCM is configured to address
 * the device by installation ID on both platforms (see `firebase_messaging_installation_id_enabled`
 * in the Android manifest and `FirebaseMessagingInstallationIdEnabled` in the iOS Info.plist).
 */
interface PushNotificationManager {
    /** The installation ID, or blank when Firebase couldn't provide one — never fails a login. */
    suspend fun getToken(): String
}
