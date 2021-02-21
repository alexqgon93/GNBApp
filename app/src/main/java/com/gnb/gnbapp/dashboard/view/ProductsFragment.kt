package com.gnb.gnbapp.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gnb.gnbapp.main.MainActivityEvents
import com.gnb.gnbapp.main.MainActivityStateView
import com.gnb.gnbapp.main.MainViewModel
import com.gnb.gnbapp.R
import com.gnb.gnbapp.components.ProductsRecyclerView
import com.gnb.gnbapp.data.model.ProductElement
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProductsFragment : Fragment() {

    private val activityViewModel by sharedViewModel<MainViewModel>()
    private lateinit var productsRecyclerView: ProductsRecyclerView
    private val adapterProductsList by lazy { ProductsAdapter { activityViewModel.processEvent(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStateView()
        initUI(view = view)
        activityViewModel.processEvent(MainActivityEvents.OnGetData)

    }

    private fun initUI(view: View) {
        productsRecyclerView =
            view.findViewById<ProductsRecyclerView>(R.id.ratesRecyclerView).apply {
                title = R.string.title_list_products
                adapter = this@ProductsFragment.adapterProductsList
            }
    }

    private fun observeStateView() {
        activityViewModel.stateView.observe(viewLifecycleOwner, Observer { onStateViewChanged(it) })
    }

    private fun onStateViewChanged(stateView: MainActivityStateView) {
        when (stateView) {
            is MainActivityStateView.ShowProgressBar -> {
                showProgressBar(show = true)
            }
            is MainActivityStateView.ProductSelected -> showTransactions(stateView.product)
            is MainActivityStateView.ErrorData -> TODO()
            is MainActivityStateView.ReceivedProducts -> showProducts(stateView.products)

        }
    }

    private fun showProgressBar(show: Boolean) {
        productsRecyclerView.progressBar(show)
    }

    private fun showProducts(products: MutableList<ProductElement>) {
        showProgressBar(show = false)
        adapterProductsList.submitList(productsList = products)
    }

    private fun showTransactions(product: ProductElement) {
        findNavController().navigate(ProductsFragmentDirections.onClickProduct(product.sku))
    }
}