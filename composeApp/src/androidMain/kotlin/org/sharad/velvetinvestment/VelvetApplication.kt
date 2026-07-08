package org.sharad.velvetinvestment

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.koin.android.ext.koin.androidContext
import org.sharad.velvetinvestment.utils.di.initializeKoin

class VelvetApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@VelvetApplication)
        }
        NotifierManager.initialize(

            NotificationPlatformConfiguration.Android(

                notificationIconResId = R.drawable.app_icon,

                showPushNotification = true

            )

        )
    }
}