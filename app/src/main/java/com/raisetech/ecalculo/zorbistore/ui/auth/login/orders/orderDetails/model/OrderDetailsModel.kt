package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.model


data class OrderDetailsModel(
    var product:String,
    var quantity:Int,
    var rate:String,
    var total: Double?
)