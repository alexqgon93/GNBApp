package com.gnb.gnbapp.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gnb.gnbapp.R
import com.gnb.gnbapp.components.ProductsRecyclerView
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.Transactions
import com.gnb.gnbapp.products.ProductsAdapter
import com.gnb.gnbapp.products.model.ProductEvents
import com.gnb.gnbapp.products.model.ProductStateView
import com.gnb.gnbapp.products.model.ProductsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    private val activityViewModel by viewModel<ProductsViewModel>()
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
        activityViewModel.processEvent(ProductEvents.OnGetData)
    }

    private fun initUI(view: View) {
        productsRecyclerView =
            view.findViewById<ProductsRecyclerView>(R.id.ratesRecyclerView).apply {
                title = R.string.title_list_products
                adapter = this@ProductsFragment.adapterProductsList
            }
    }

    private fun observeStateView() {
        activityViewModel.stateView.observe(viewLifecycleOwner, { onStateViewChanged(it) })
    }

    private fun onStateViewChanged(stateView: ProductStateView) {
        when (stateView) {
            is ProductStateView.ShowProductProgressBar -> {
                showProgressBar(show = true)
            }
            is ProductStateView.ProductSelected -> showTransactions(
                stateView.product,
                stateView.productsResponse
            )
            is ProductStateView.ReceivedProducts -> showProducts(stateView.products)
            ProductStateView.ErrorData -> TODO()
        }
    }

    private fun showProgressBar(show: Boolean) {
        productsRecyclerView.progressBar(show)
    }

    private fun showProducts(products: MutableList<ProductElement>) {
        showProgressBar(show = false)
        adapterProductsList.submitList(productsList = products)
    }

    private fun showTransactions(product: ProductElement, productsResponse: Transactions) {
        makeText(
            context,
            getString(R.string.message_transition_products_to_transactions),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(
            ProductsFragmentDirections.onClickProduct(
                product = product.sku,
                list = productsResponse
            )
        )
    }
}
