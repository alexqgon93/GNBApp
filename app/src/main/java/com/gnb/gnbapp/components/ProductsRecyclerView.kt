package com.gnb.gnbapp.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnb.gnbapp.R
import com.gnb.gnbapp.utils.inflate

class ProductsRecyclerView(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {
    init {
        inflate(R.layout.rates_recycler_view, true)
    }

    private val titleView by lazy { findViewById<TextView>(R.id.titleView) }
    private val recyclerView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.ratesList).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }

    var title: Int = 0
        set(value) = titleView.setText(value)

    var adapter: RecyclerView.Adapter<*>?
        set(value) {
            recyclerView.adapter = value
        }
        get() = recyclerView.adapter

    fun progressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}