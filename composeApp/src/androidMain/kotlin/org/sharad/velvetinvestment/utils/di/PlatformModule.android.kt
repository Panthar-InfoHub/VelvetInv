package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sharad.velvetinvestment.utils.AndroidBrowserLauncher
import org.sharad.velvetinvestment.utils.AndroidSharedPreferences
import org.sharad.velvetinvestment.utils.BrowserLauncher
import org.sharad.velvetinvestment.utils.DeviceInfoRetrieverAndroid
import org.sharad.velvetinvestment.utils.PdfDownloadManager
import org.sharad.velvetinvestment.utils.PdfDownloader
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.storage.SharedPreference

actual val platformModule: Module
    get() = module {
        single<DeviceInfoRetriever> { DeviceInfoRetrieverAndroid(get()) }
        single <SharedPreference>{ AndroidSharedPreferences(get()) }
        single<PdfDownloadManager> { PdfDownloader(get()) }
        single<BrowserLauncher> { AndroidBrowserLauncher(get()) }

    }