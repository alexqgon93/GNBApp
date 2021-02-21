package com.gnb.gnbapp.transactions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.gnb.gnbapp.R

class TransactionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        initUI()
        findNavController(R.id.nav_host_fragment_transactions).apply {
            setGraph(R.navigation.transaction_nav_graph, intent.extras)
        }
    }

    private fun initUI() {
        findViewById<Toolbar>(R.id.transactionsToolbar).apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
    }
}
