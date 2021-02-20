package com.gnb.gnbapp.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.RatesElement
import com.gnb.gnbapp.data.repository.MainRepository
import kotlinx.coroutines.launch

sealed class MainActivityEvents {
    object OnGetData : MainActivityEvents()
    class OnProductSelected(val product: String) : MainActivityEvents()
}

sealed class MainActivityStateView {
    object ShowProgressBar : MainActivityStateView()
    class ProductSelected(val product: String) : MainActivityStateView()
}

class MainViewModel(
    private val mainRepository: MainRepository,
    private val mutableStateView: MutableLiveData<MainActivityStateView> = MutableLiveData()
) : ViewModel() {

    val stateView: LiveData<MainActivityStateView>
        get() = mutableStateView
    private lateinit var ratesList: List<RatesElement>
    private lateinit var productsList: List<ProductElement>

    fun processEvent(event: MainActivityEvents) {
        mutableStateView.postValue(internalProcessEvent(event))
    }

    private fun internalProcessEvent(event: MainActivityEvents): MainActivityStateView {
        return when (event) {
            is MainActivityEvents.OnGetData -> {
                getRatesData()
                getProductsData()
                MainActivityStateView.ShowProgressBar
            }
            is MainActivityEvents.OnProductSelected -> {
                MainActivityStateView.ProductSelected(event.product)
            }
        }
    }

    private fun getRatesData() {
        viewModelScope.launch {
            val products = mainRepository.getProducts()
            if (products.isNotEmpty()) {
                productsList = products
            } else {
                Log.d("TAG", "error on call")
            }
        }
    }

    private fun getProductsData() {
        viewModelScope.launch {
            val rates = mainRepository.getRates()
            if (rates.isNotEmpty()) {
                ratesList = rates
            } else {
                Log.d("TAG", "error on call")
            }
        }
    }
}