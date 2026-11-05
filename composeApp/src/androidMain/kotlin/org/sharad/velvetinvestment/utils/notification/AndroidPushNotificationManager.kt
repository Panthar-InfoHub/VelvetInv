package org.sharad.velvetinvestment.utils.notification

import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await

/** The Firebase Installation ID; blank when Firebase can't provide one, so login still goes ahead. */
class AndroidPushNotificationManager : PushNotificationManager {
    override suspend fun getToken(): String =
        runCatching { FirebaseInstallations.getInstance().id.await() }.getOrDefault("")
}
