package org.sharad.velvetinvestment.utils.notification

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.utils.AppEventsController

/**
 * Reacts to an FCM push while the app process is alive (JN). Each platform passes the `txn` field
 * of the message's data: Android from its messaging service, iOS from its notification delegate.
 */
object PushNotificationHandler {

    private const val TXN_KYC = "kyc"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun onPushReceived(txn: String?) {
        scope.launch {
            // Velvet: any push may be a new inbox item, so the home bell gets its unread dot
            // (what KMPNotifier's listener in App.kt used to do).
            AppEventsController.sendNotificationMarkEvent()

            when (txn) {
                // KYC was verified on the server: home reloads in place so the pending card updates.
                TXN_KYC -> AppEventsController.sendKycSilentRefreshEvent()
            }
        }
    }
}
