package com.gnb.gnbapp.data.model

import com.google.gson.annotations.SerializedName

data class RatesElement(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("rate") val rate: String,
)