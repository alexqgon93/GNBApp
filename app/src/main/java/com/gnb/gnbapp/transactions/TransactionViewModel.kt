package com.gnb.gnbapp.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.Transactions

sealed class TransactionEvents {
    class OnGetData(val product: String, val transactions: Transactions) : TransactionEvents()
    object OnErrorData : TransactionEvents()
}

sealed class TransactionStateView {
    object ShowTransactionsProgressBar : TransactionStateView()
    object ShowErrorData : TransactionStateView()
    class FilteredTransactions(val transactions: MutableList<ProductElement>) :
        TransactionStateView()
}

class TransactionViewModel(
    private val mutableStateView: MutableLiveData<TransactionStateView> = MutableLiveData()
) : ViewModel() {

    val stateView: LiveData<TransactionStateView>
        get() = mutableStateView

    fun processEvent(event: TransactionEvents) {
        mutableStateView.postValue(internalProcessEvent(event))
    }

    private fun internalProcessEvent(event: TransactionEvents): TransactionStateView {
        return when (event) {
            is TransactionEvents.OnErrorData -> TransactionStateView.ShowErrorData
            is TransactionEvents.OnGetData -> {
                givenTransactions(event.product, event.transactions.transactions)
            }
        }
    }

    private fun givenTransactions(
        product: String,
        transactions: List<ProductElement>
    ): TransactionStateView.FilteredTransactions {
        val filteredProducts = transactions.filter { it.sku == product }
        return TransactionStateView.FilteredTransactions(transactions = filteredProducts as MutableList<ProductElement>)
    }
}
