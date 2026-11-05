package org.sharad.velvetinvestment.utils.di

import org.sharad.velvetinvestment.utils.location.IosLocationProvider
import org.sharad.velvetinvestment.utils.location.LocationProvider

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sharad.velvetinvestment.shared.data.local.database.VelvetDatabase
import org.sharad.velvetinvestment.shared.data.local.database.createIosDatabase
import org.sharad.velvetinvestment.utils.BrowserLauncher
import org.sharad.velvetinvestment.utils.deeplink.ExternalAppLauncher
import org.sharad.velvetinvestment.utils.deeplink.IosExternalAppLauncher
import org.sharad.velvetinvestment.utils.DeviceInfoRetrieverIos
import org.sharad.velvetinvestment.utils.IOSBrowserLauncher
import org.sharad.velvetinvestment.utils.IosSharedPreferences
import org.sharad.velvetinvestment.utils.PdfDownloadManager
import org.sharad.velvetinvestment.utils.PdfDownloaderIos
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.pdfutils.PdfViewer
import org.sharad.velvetinvestment.utils.pdfutils.PdfViewerIos
import org.sharad.velvetinvestment.utils.storage.SharedPreference

actual val platformModule: Module
    get() = module {
        single<DeviceInfoRetriever>{ DeviceInfoRetrieverIos() }
        single <SharedPreference>{ IosSharedPreferences() }
        single<BrowserLauncher> { IOSBrowserLauncher() }
        single<ExternalAppLauncher> { IosExternalAppLauncher() }
        single<PdfDownloadManager> { PdfDownloaderIos(get()) }
        single<PdfViewer> { PdfViewerIos() }
        single<VelvetDatabase> { createIosDatabase() }
        single<LocationProvider> { IosLocationProvider() }

    }