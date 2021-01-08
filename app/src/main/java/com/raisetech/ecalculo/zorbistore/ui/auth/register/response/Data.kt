package com.raisetech.ecalculo.zorbistore.ui.auth.register.response

data class Data(
    val address: String,
    val country: String,
    val deleted: Boolean,
    val email: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val password: String,
    val primaryContactNumber: String,
    val secondaryContactNumber: String,
    val shippingAddress: String
)