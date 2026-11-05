package org.sharad.velvetinvestment

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.dsl.module
import org.sharad.velvetinvestment.utils.di.initializeKoin
import org.sharad.velvetinvestment.utils.notification.FirebaseInstallationIdProvider
import org.sharad.velvetinvestment.utils.notification.IosPushNotificationManager
import org.sharad.velvetinvestment.utils.notification.PushNotificationManager

/** [installationIdProvider] comes from Swift, which owns the Firebase SDK (JN). */
fun MainViewController(installationIdProvider: FirebaseInstallationIdProvider) = ComposeUIViewController(
    configure = {
        initializeKoin {
            modules(
                module {
                    single<PushNotificationManager> { IosPushNotificationManager(installationIdProvider) }
                }
            )
        }
    }
) {
    App()
}
