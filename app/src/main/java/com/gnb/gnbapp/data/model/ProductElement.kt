package com.gnb.gnbapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductElement(
    @SerializedName("sku") val sku: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("currency") val currency: String
) : Serializable

class Transactions(val transactions: List<ProductElement>) : Serializable
