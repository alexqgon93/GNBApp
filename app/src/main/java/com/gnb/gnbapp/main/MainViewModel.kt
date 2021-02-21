package com.gnb.gnbapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.RatesElement
import com.gnb.gnbapp.data.model.Transactions
import com.gnb.gnbapp.data.repository.MainRepository
import kotlinx.coroutines.launch

sealed class MainActivityEvents {
    object OnGetData : MainActivityEvents()
    object OnErrorData : MainActivityEvents()
    class OnProductSelected(val product: ProductElement) : MainActivityEvents()
    class OnReceivedProducts(val products: MutableList<ProductElement>) : MainActivityEvents()
}

sealed class MainActivityStateView {
    object ShowProductProgressBar : MainActivityStateView()
    object ErrorData : MainActivityStateView()
    class ProductSelected(val product: ProductElement, val productsResponse: Transactions) :
        MainActivityStateView()

    class ReceivedProducts(val products: MutableList<ProductElement>) : MainActivityStateView()
}

class MainViewModel(
    private val mainRepository: MainRepository,
    private val mutableStateView: MutableLiveData<MainActivityStateView> = MutableLiveData()
) : ViewModel() {

    val stateView: LiveData<MainActivityStateView>
        get() = mutableStateView
    private lateinit var ratesList: List<RatesElement>
    private lateinit var productsResponse: List<ProductElement>
    private val productsList: MutableList<ProductElement> = ArrayList()

    fun processEvent(event: MainActivityEvents) {
        mutableStateView.postValue(internalProcessEvent(event))
    }

    private fun internalProcessEvent(event: MainActivityEvents): MainActivityStateView {
        return when (event) {
            is MainActivityEvents.OnGetData -> {
                getProductsData()
                getRatesData()
                MainActivityStateView.ShowProductProgressBar
            }
            is MainActivityEvents.OnProductSelected -> {
                MainActivityStateView.ProductSelected(event.product, Transactions(productsResponse))
            }
            is MainActivityEvents.OnReceivedProducts -> MainActivityStateView.ReceivedProducts(event.products)
            is MainActivityEvents.OnErrorData -> MainActivityStateView.ErrorData
        }
    }

    private fun getProductsData() {
        viewModelScope.launch {
            val products = mainRepository.getProducts()
            if (products.isNotEmpty()) {
                productsResponse = products
                // parseCurrency(products)
                getProducts(products)
            } else {
                processEvent(MainActivityEvents.OnErrorData)
            }
        }
    }

    private fun getRatesData() {
        viewModelScope.launch {
            val rates = mainRepository.getRates()
            if (rates.isNotEmpty()) {
                ratesList = rates
            } else {
                processEvent(MainActivityEvents.OnErrorData)
            }
        }
    }

    private fun getProducts(products: List<ProductElement>) {
        products.forEach { item ->
            if (productsList.isEmpty()) {
                productsList.add(item)
            } else {
                val found = productsList.any { productElement -> productElement.sku == item.sku }
                if (!found) {
                    productsList.add(item)
                }
            }
        }
        processEvent(MainActivityEvents.OnReceivedProducts(productsList))
    }
}
