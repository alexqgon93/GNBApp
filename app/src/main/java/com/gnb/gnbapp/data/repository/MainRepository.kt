package com.gnb.gnbapp.data.repository

import com.gnb.gnbapp.data.api.ApiService

class MainRepository(private val apiService: ApiService) {
    suspend fun getRates() = apiService.getRates()
    suspend fun getProducts() = apiService.getProducts()
}
