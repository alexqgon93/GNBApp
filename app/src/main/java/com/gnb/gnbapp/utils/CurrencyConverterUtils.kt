package com.gnb.gnbapp.utils

import com.gnb.gnbapp.data.model.DirectConversion
import com.gnb.gnbapp.data.model.ProductElement
import com.gnb.gnbapp.data.model.RatesElement
import com.gnb.gnbapp.products.model.ProductsViewModel
import kotlin.math.round

fun parseProductsToEur(
    products: List<ProductElement>,
    ratesList: List<RatesElement>
): MutableList<ProductElement> {
    val list: MutableList<ProductElement> = ArrayList()
    products.forEach { productElement ->
        if (productElement.currency == ProductsViewModel.CURRENCY) {
            list.add(productElement)
        } else {
            val newProductElement = ProductElement(
                productElement.sku,
                changeCurrency(
                    productElement.currency,
                    ProductsViewModel.CURRENCY,
                    productElement.amount,
                    ratesList
                ),
                ProductsViewModel.CURRENCY
            )
            list.add(newProductElement)
        }
    }
    return list
}

private fun changeCurrency(
    from: String,
    to: String,
    amount: String,
    ratesList: List<RatesElement>
): String {
    var amout = ""
    val checkConversion = checkDirectConversion(from, to, ratesList)
    return if (checkConversion.isDirect) {
        (round((amount.toDouble() * checkConversion.rate.toDouble()) * 100) / 100).toString()
    } else {
        for (value in ratesList) {
            if (to == value.to) {
                amout =
                    (round(
                        (changeCurrency(
                            from,
                            value.from,
                            amount,
                            ratesList
                        ).toDouble() * value.rate.toDouble()) * 100
                    ) / 100).toString()
                break
            }
        }
        amout
    }
}

private fun checkDirectConversion(
    from: String,
    to: String,
    ratesList: List<RatesElement>
): DirectConversion {
    val directConversion = DirectConversion(false, "")
    for (rate in ratesList) {
        if (rate.from == from && rate.to == to) {
            directConversion.isDirect = true
            directConversion.rate = rate.rate
        }
    }
    return directConversion
}
