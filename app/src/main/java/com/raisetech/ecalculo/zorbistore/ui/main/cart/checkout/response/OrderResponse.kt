package com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("data")
    val data:String,
    @SerializedName("message")
    val message:String,
    @SerializedName("status")
    val status:String

    )
