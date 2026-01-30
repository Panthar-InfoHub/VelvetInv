package org.sharad.velvetinvestment

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.sharad.velvetinvestment.utils.di.initializeKoin

class VelvetApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@VelvetApplication)
        }
    }
}