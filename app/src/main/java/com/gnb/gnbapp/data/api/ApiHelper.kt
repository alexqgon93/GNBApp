package com.gnb.gnbapp.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getRates() = apiService.getRates()
}