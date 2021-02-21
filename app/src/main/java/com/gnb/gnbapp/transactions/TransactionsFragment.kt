package com.gnb.gnbapp.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gnb.gnbapp.R
import com.gnb.gnbapp.components.ProductsRecyclerView
import com.gnb.gnbapp.main.MainActivityStateView
import com.gnb.gnbapp.main.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class TransactionsFragment : Fragment() {

    private val activityViewModel by sharedViewModel<MainViewModel>()
    private lateinit var transactionsRecyclerView: ProductsRecyclerView
    private val adapterTransactionsList by lazy { TransactionsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStateView()
        initUI(view = view)
    }

    private fun initUI(view: View) {
        transactionsRecyclerView =
            view.findViewById<ProductsRecyclerView>(R.id.transactionsRecyclerView).apply {
                title = R.string.title_list_transactions
                adapter = this@TransactionsFragment.adapterTransactionsList
            }
    }

    private fun observeStateView() {
        activityViewModel.stateView.observe(viewLifecycleOwner, Observer { onStateViewChanged(it) })
    }

    private fun onStateViewChanged(stateView: MainActivityStateView) {
        when (stateView) {
            is MainActivityStateView.ShowProgressBar -> Unit
            is MainActivityStateView.ProductSelected -> Unit
            is MainActivityStateView.ErrorData -> Unit
            is MainActivityStateView.ReceivedProducts -> Unit
        }
    }
}