package com.gnb.gnbapp.data.model

import com.google.gson.annotations.SerializedName

data class ProductElement(
    @SerializedName("sku") val sku: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("currency") val currency: String
)