package com.raisetech.ecalculo.zorbistore.ui.auth.login.response

data class Data(
    val billAmount: Double,
    val ecommerceOrderDetailsList: List<Any>,
    val id: Int,
    val nonTaxableAmount: Double,
    val orderDate: String,
    val orderNo: Int,
    val paymentMethod: String,
    val status: String,
    val subTotalAmount: Double
)