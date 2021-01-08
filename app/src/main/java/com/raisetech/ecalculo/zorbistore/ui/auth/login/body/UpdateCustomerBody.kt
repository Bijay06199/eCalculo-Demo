package com.raisetech.ecalculo.zorbistore.ui.auth.login.body

data class UpdateCustomerBody(
    val address: String?,
    val country: String,
    val firstName: String,
    val lastName: String,
    val primaryContactNumber: String?,
    val secondaryContactNumber: String?,
    val shippingAddress: String?
)