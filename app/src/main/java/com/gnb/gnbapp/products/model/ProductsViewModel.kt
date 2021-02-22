package com.gnb.gnbapp.products.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.RatesElement
import com.gnb.gnbapp.data.model.Transactions
import com.gnb.gnbapp.data.repository.MainRepository
import com.gnb.gnbapp.utils.parseProductsToEur
import kotlin.math.round
import kotlinx.coroutines.launch

sealed class ProductEvents {
    object OnGetData : ProductEvents()
    object OnErrorData : ProductEvents()
    class OnProductSelected(val product: ProductElement) : ProductEvents()
    class OnReceivedProducts(val products: MutableList<ProductElement>) : ProductEvents()
}

sealed class ProductStateView {
    object ShowProductProgressBar : ProductStateView()
    object ErrorData : ProductStateView()
    class ProductSelected(val product: ProductElement, val productsResponse: Transactions) :
        ProductStateView()

    class ReceivedProducts(val products: MutableList<ProductElement>) : ProductStateView()
}

class ProductsViewModel(
    private val mainRepository: MainRepository,
    private val mutableStateView: MutableLiveData<ProductStateView> = MutableLiveData()
) : ViewModel() {

    companion object {
        const val CURRENCY = "EUR"
    }

    val stateView: LiveData<ProductStateView>
        get() = mutableStateView
    private lateinit var ratesList: List<RatesElement>
    private lateinit var productsResponse: List<ProductElement>
    private val productsList: MutableList<ProductElement> = ArrayList()

    fun processEvent(event: ProductEvents) {
        mutableStateView.postValue(internalProcessEvent(event))
    }

    private fun internalProcessEvent(event: ProductEvents): ProductStateView {
        return when (event) {
            is ProductEvents.OnGetData -> {
                getProductsData()
                getRatesData()
                ProductStateView.ShowProductProgressBar
            }
            is ProductEvents.OnProductSelected -> {
                ProductStateView.ProductSelected(event.product, Transactions(productsResponse))
            }
            is ProductEvents.OnReceivedProducts -> ProductStateView.ReceivedProducts(event.products)
            is ProductEvents.OnErrorData -> ProductStateView.ErrorData
        }
    }

    private fun getProductsData() {
        viewModelScope.launch {
            val products = mainRepository.getProducts()
            if (products.isNotEmpty()) {
                productsResponse = products
                getProducts(parseProductsToEur(products, ratesList))
            } else {
                processEvent(ProductEvents.OnErrorData)
            }
        }
    }

    private fun getRatesData() {
        viewModelScope.launch {
            val rates = mainRepository.getRates()
            if (rates.isNotEmpty()) {
                ratesList = rates
            } else {
                processEvent(ProductEvents.OnErrorData)
            }
        }
    }

    private fun getProducts(productsConvertedList: MutableList<ProductElement>) {
        productsConvertedList.forEach { item ->
            if (productsList.isEmpty()) {
                productsList.add(item)
            } else {
                val found = productsList.any { productElement -> productElement.sku == item.sku }
                if (!found) {
                    productsList.add(item)
                } else {
                    productsList.forEach { product ->
                        if (item.sku == product.sku) {
                            product.amount =
                                (round((product.amount.toDouble() + item.amount.toDouble()) * 100) / 100).toString()
                        }
                    }
                }
            }
        }
        processEvent(ProductEvents.OnReceivedProducts(productsList))
    }
}
