package com.gnb.gnbapp.data.api

import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.RatesElement
import retrofit2.http.GET

interface ApiService {
    @GET("rates.json")
    suspend fun getRates(): List<RatesElement>

    @GET("transactions.json")
    suspend fun getProducts(): List<ProductElement>
}

