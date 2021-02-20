package com.gnb.gnbapp

import android.content.Context
import com.gnb.gnbapp.data.api.RetrofitBuilder
import com.gnb.gnbapp.data.repository.MainRepository
import com.gnb.gnbapp.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class GNBConfigurationApplication {

    var modules = module {
        viewModel { MainViewModel(mainRepository = MainRepository(RetrofitBuilder.apiService)) }
    }

    fun initializeDependencyInjection(context: Context) {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(context)
            modules(modules)
        }
    }
}