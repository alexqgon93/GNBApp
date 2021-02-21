package com.gnb.gnbapp.products

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gnb.gnbapp.R
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.main.MainActivityEvents
import com.gnb.gnbapp.utils.inflate

class ProductsAdapter(private val onEventListener: (MainActivityEvents) -> Unit) :
    RecyclerView.Adapter<ProductsViewHolder>() {

    private val items: MutableList<ProductElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        getItemPosition(position).let {
            holder.bind(it, onEventListener)
        }
    }

    private fun getItemPosition(position: Int) = items[position]

    override fun getItemCount(): Int = items.size

    fun submitList(productsList: MutableList<ProductElement>) {
        items.apply {
            clear()
            addAll(productsList)
        }
        notifyItemRangeChanged(0, productsList.size)
    }
}

class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val contentView by lazy { itemView.findViewById<View>(R.id.contentView) }
    private val titleProduct: TextView = view.findViewById(R.id.titleProduct)
    private val totalPurchased: TextView = view.findViewById(R.id.totalPurchased)

    companion object {
        fun create(parent: ViewGroup): ProductsViewHolder {
            return ProductsViewHolder(parent.inflate((R.layout.product_view_holder)))
        }
    }

    fun bind(item: ProductElement, onEventListener: (MainActivityEvents) -> Unit) {
        contentView.setOnClickListener { onEventListener(MainActivityEvents.OnProductSelected(item)) }
        titleProduct.text = item.sku
        totalPurchased.text = "${item.amount} EUR"
    }
}
