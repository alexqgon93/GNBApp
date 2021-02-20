package com.gnb.gnbapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class MainActivityEvents {
    object OnGetData : MainActivityEvents()
    class OnProductSelected(val product: String) : MainActivityEvents()
}

sealed class MainActivityStateView {
    object ShowProgressBar : MainActivityStateView()
    class ProductSelected(val product: String) : MainActivityStateView()
}

class MainViewModel(
    private val mutableStateView: MutableLiveData<MainActivityStateView> = MutableLiveData()
) : ViewModel() {

    val stateView: LiveData<MainActivityStateView>
        get() = mutableStateView


    fun processEvent(event: MainActivityEvents) {
        mutableStateView.postValue(internalProcessEvent(event))
    }

    private fun internalProcessEvent(event: MainActivityEvents): MainActivityStateView {
        return when (event) {
            is MainActivityEvents.OnGetData -> {
                MainActivityStateView.ShowProgressBar
            }
            is MainActivityEvents.OnProductSelected -> {
                MainActivityStateView.ProductSelected(event.product)
            }
        }
    }
}