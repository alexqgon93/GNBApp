package com.gnb.gnbapp

import android.app.Application

class GNBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GNBConfigurationApplication().initializeDependencyInjection(this)
    }
}
