package com.raisetech.ecalculo.zorbistore.ui.auth.login.response

data class CustomerOrderResponse(
    val `data`: List<Data>,
    val message: String,
    val status: String
)