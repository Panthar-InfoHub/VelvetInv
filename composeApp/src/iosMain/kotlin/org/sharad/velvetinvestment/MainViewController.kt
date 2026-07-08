package org.sharad.velvetinvestment

import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.sharad.velvetinvestment.utils.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initializeKoin()
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Ios(
                askNotificationPermissionOnStart = false,
                showPushNotification = true
            )
        )
    }
) {
    App()
}