package com.gnb.gnbapp

import android.content.Context
import com.gnb.gnbapp.data.api.RetrofitBuilder
import com.gnb.gnbapp.data.repository.MainRepository
import com.gnb.gnbapp.products.model.ProductsViewModel
import com.gnb.gnbapp.transactions.model.TransactionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class GNBConfigurationApplication {

    var modules = module {
        viewModel { ProductsViewModel(mainRepository = MainRepository(RetrofitBuilder.apiService)) }
        viewModel { TransactionViewModel() }
    }

    fun initializeDependencyInjection(context: Context) {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(context)
            modules(modules)
        }
    }
}
