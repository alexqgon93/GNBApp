package com.gnb.gnbapp.transactions.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.gnb.gnbapp.R
import com.gnb.gnbapp.components.ProductsRecyclerView
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.transactions.*
import com.gnb.gnbapp.transactions.model.TransactionEvents
import com.gnb.gnbapp.transactions.model.TransactionStateView
import com.gnb.gnbapp.transactions.model.TransactionViewModel
import com.gnb.gnbapp.transactions.model.TransactionsAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class TransactionsFragment : Fragment() {

    private val viewModel by viewModel<TransactionViewModel>()
    private lateinit var transactionsRecyclerView: ProductsRecyclerView
    private val adapterTransactionsList by lazy { TransactionsAdapter() }
    private val args: TransactionsFragmentArgs by navArgs()
    private lateinit var productType: TextView
    private lateinit var productAmount: TextView

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
        viewModel.processEvent(TransactionEvents.OnGetData(args.product, args.list))
    }

    private fun initUI(view: View) {
        transactionsRecyclerView =
            view.findViewById<ProductsRecyclerView>(R.id.transactionsRecyclerView).apply {
                adapter = this@TransactionsFragment.adapterTransactionsList
            }
        productType = view.findViewById(R.id.productType)
        productAmount = view.findViewById(R.id.productAmount)
        productType.text = getString(R.string.products_title_transaction_screen, args.product)
        productAmount.text =
            getString(R.string.total_purchased_transaction_screen, args.totalPurchased)
    }

    private fun showTransactionsProgressBar(show: Boolean) {
        transactionsRecyclerView.progressBar(show)
    }

    private fun observeStateView() {
        viewModel.stateView.observe(viewLifecycleOwner, { onStateViewChanged(it) })
    }

    private fun onStateViewChanged(stateView: TransactionStateView) {
        when (stateView) {
            is TransactionStateView.ShowTransactionsProgressBar -> showTransactionsProgressBar(show = true)
            is TransactionStateView.FilteredTransactions -> showTransactions(transactions = stateView.transactions)
            is TransactionStateView.ShowErrorData -> showDialog()
        }
    }

    private fun showTransactions(transactions: MutableList<ProductElement>) {
        showTransactionsProgressBar(show = false)
        adapterTransactionsList.submitList(transactionsList = transactions)
    }

    private fun showDialog() {
        AlertDialog.Builder(context).setTitle(getString(R.string.errorTitle))
            .setMessage(getString(R.string.dialog_error_message))
            .setPositiveButton(getString(R.string.dialog_positive_button)) { _, _ ->
                activity?.onBackPressed()
            }.show()
    }
}
