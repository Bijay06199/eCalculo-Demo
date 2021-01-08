package com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.body

data class OrderBody(
        val billAmount: Double?,
        val customer: Int?,
        val discountAmount: Double?,
        val ecommerceOrderDetailsList: List<EcommerceOrderDetails>?,
        val nonTaxableAmount: Double?,
        val paymentMethod: String?,
        val subTotalAmount: Double?,
        val taxableAmount: Double?,
        val vatAmount: Double?,
        val vatPercentage: Double?
)