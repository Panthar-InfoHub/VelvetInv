package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sharad.velvetinvestment.utils.BrowserLauncher
import org.sharad.velvetinvestment.utils.DeviceInfoRetrieverIos
import org.sharad.velvetinvestment.utils.IOSBrowserLauncher
import org.sharad.velvetinvestment.utils.IosSharedPreferences
import org.sharad.velvetinvestment.utils.PdfDownloadManager
import org.sharad.velvetinvestment.utils.PdfDownloaderIos
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.storage.SharedPreference

actual val platformModule: Module
    get() = module {
        single<DeviceInfoRetriever>{ DeviceInfoRetrieverIos() }
        single <SharedPreference>{ IosSharedPreferences() }
        single<BrowserLauncher> { IOSBrowserLauncher() }
        single<PdfDownloadManager> { PdfDownloaderIos() }
    }