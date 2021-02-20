package com.gnb.gnbapp.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gnb.gnbapp.main.MainActivityEvents
import com.gnb.gnbapp.main.MainActivityStateView
import com.gnb.gnbapp.main.MainViewModel
import com.gnb.gnbapp.R
import com.gnb.gnbapp.components.RatesRecyclerView
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProductsFragment : Fragment() {

    private val activityViewModel by sharedViewModel<MainViewModel>()
    private lateinit var ratesRecyclerView: RatesRecyclerView

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
        initUI(view)
        activityViewModel.processEvent(MainActivityEvents.OnGetData)

    }

    private fun initUI(view: View) {
        ratesRecyclerView = view.findViewById<RatesRecyclerView>(R.id.ratesRecyclerView).apply {
            title = R.string.title_list_products
            adapter = ProductsAdapter(context)
        }
    }

    private fun observeStateView() {
        activityViewModel.stateView.observe(viewLifecycleOwner, Observer { onStateViewChanged(it) })
    }

    private fun onStateViewChanged(stateView: MainActivityStateView) {
        when (stateView) {
            is MainActivityStateView.ShowProgressBar -> {
                showProgressBar(true)
            }
            is MainActivityStateView.ProductSelected -> TODO()
        }
    }

    private fun showProgressBar(show: Boolean) {
        ratesRecyclerView.progressBar(show)
    }

}