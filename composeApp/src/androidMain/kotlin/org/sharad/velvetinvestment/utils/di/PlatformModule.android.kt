package org.sharad.velvetinvestment.utils.di

import org.sharad.velvetinvestment.utils.location.AndroidLocationProvider
import org.sharad.velvetinvestment.utils.location.LocationProvider

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sharad.velvetinvestment.shared.data.local.database.VelvetDatabase
import org.sharad.velvetinvestment.shared.data.local.database.createAndroidDatabase
import org.sharad.velvetinvestment.utils.AndroidBrowserLauncher
import org.sharad.velvetinvestment.utils.AndroidSharedPreferences
import org.sharad.velvetinvestment.utils.BrowserLauncher
import org.sharad.velvetinvestment.utils.deeplink.AndroidExternalAppLauncher
import org.sharad.velvetinvestment.utils.deeplink.ExternalAppLauncher
import org.sharad.velvetinvestment.utils.notification.AndroidPushNotificationManager
import org.sharad.velvetinvestment.utils.notification.PushNotificationManager
import org.sharad.velvetinvestment.utils.DeviceInfoRetrieverAndroid
import org.sharad.velvetinvestment.utils.PdfDownloadManager
import org.sharad.velvetinvestment.utils.PdfDownloader
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.pdfutils.PdfViewer
import org.sharad.velvetinvestment.utils.pdfutils.PdfViewerAndroid
import org.sharad.velvetinvestment.utils.storage.SharedPreference

actual val platformModule: Module
    get() = module {
        single<DeviceInfoRetriever> { DeviceInfoRetrieverAndroid(get()) }
        single <SharedPreference>{ AndroidSharedPreferences(get()) }
        single<PdfDownloadManager> { PdfDownloader(get(),get()) }
        single<BrowserLauncher> { AndroidBrowserLauncher(get()) }
        single<ExternalAppLauncher> { AndroidExternalAppLauncher(get()) }
        single<PushNotificationManager> { AndroidPushNotificationManager() }
        single<PdfViewer> { PdfViewerAndroid(get()) }
        single<VelvetDatabase> { createAndroidDatabase(get()) }
        single<LocationProvider> { AndroidLocationProvider(get()) }

    }