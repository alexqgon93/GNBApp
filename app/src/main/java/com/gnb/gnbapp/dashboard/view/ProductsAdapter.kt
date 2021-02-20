package com.gnb.gnbapp.dashboard.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gnb.gnbapp.R
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.main.MainActivityEvents

class ProductsAdapter(private val context: Context) : RecyclerView.Adapter<ProductsViewHolder>() {

    private val items: MutableList<ProductElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_view_holder, parent, false)
        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val data = items[position]
        holder.titleProduct.text = data.sku
        holder.totalPurchased.text = data.amount

        holder.itemView.setOnClickListener {
            MainActivityEvents.OnProductSelected(data.sku)
        }
    }

    override fun getItemCount(): Int = items.size
}


class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleProduct: TextView = view.findViewById(R.id.titleProduct)
    val totalPurchased: TextView = view.findViewById(R.id.totalPurchased)
}
