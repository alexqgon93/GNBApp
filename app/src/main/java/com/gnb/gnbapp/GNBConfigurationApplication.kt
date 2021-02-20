package com.gnb.gnbapp

import android.content.Context
import com.gnb.gnbapp.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class GNBConfigurationApplication {

    var modules = module {
        viewModel { MainViewModel() }
    }

    fun initializeDependencyInjection(context: Context) {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(context)
            modules(modules)
        }
    }
}