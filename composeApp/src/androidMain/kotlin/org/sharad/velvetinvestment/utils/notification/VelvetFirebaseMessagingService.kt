package org.sharad.velvetinvestment.utils.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.sharad.velvetinvestment.R
import kotlin.random.Random

/**
 * Receives FCM messages (JN). FCM addresses this install by its Firebase Installation ID
 * (`firebase_messaging_installation_id_enabled` in the manifest), which the backend got at login —
 * so there is no registration token to forward from here.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class VelvetFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "velvet_notifications"
        private const val CHANNEL_NAME = "Velvet Alerts"
        private const val CHANNEL_DESC = "Notifications for updates and transactions"
    }

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
        Log.d("FCM", "Installation ID: $installationId")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Message received: ${remoteMessage.messageId}")

        PushNotificationHandler.onPushReceived(remoteMessage.data["txn"])

        // 1. A notification payload carries its own title and body.
        remoteMessage.notification?.let { notification ->
            showNotification(notification.title ?: "Velvet", notification.body ?: "", remoteMessage.data)
            return
        }

        // 2. A data-only payload: read them from the data map.
        if (remoteMessage.data.isNotEmpty()) {
            showNotification(
                remoteMessage.data["title"] ?: "Velvet",
                remoteMessage.data["body"] ?: "",
                remoteMessage.data
            )
        }
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = CHANNEL_DESC }
            notificationManager.createNotificationChannel(channel)
        }

        // Tapping opens the app, with the message data as extras.
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(1000, 9999), notification)
    }
}
