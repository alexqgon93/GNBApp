package com.gnb.gnbapp.data.api

import com.gnb.gnbapp.data.model.RatesElement
import retrofit2.http.GET

interface ApiService {
    companion object {
        const val PATH = "rates.json"
    }

    @GET(PATH)
    suspend fun getRates(): List<RatesElement>
}