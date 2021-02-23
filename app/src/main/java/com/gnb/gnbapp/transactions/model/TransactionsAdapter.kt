package com.gnb.gnbapp.transactions.model

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gnb.gnbapp.R
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.utils.inflate

class TransactionsAdapter : RecyclerView.Adapter<ProductsViewHolder>() {

    private val items: MutableList<ProductElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        getItemPosition(position).let {
            holder.bind(it, holder.itemView.context)
        }
    }

    private fun getItemPosition(position: Int) = items[position]

    override fun getItemCount(): Int = items.size

    fun submitList(transactionsList: MutableList<ProductElement>) {
        items.apply {
            clear()
            addAll(transactionsList)
        }
        notifyItemRangeChanged(0, transactionsList.size)
    }
}

class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val titleProduct: TextView = view.findViewById(R.id.titleProduct)
    private val totalPurchased: TextView = view.findViewById(R.id.totalPurchased)

    companion object {
        fun create(parent: ViewGroup): ProductsViewHolder {
            return ProductsViewHolder(parent.inflate((R.layout.product_view_holder)))
        }
    }

    fun bind(item: ProductElement, context: Context) {
        titleProduct.text = item.sku
        totalPurchased.text =
            context.resources.getString(R.string.transaction_item, item.amount, item.currency)

    }
}
