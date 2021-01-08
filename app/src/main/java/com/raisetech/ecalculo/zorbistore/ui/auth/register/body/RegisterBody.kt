package com.raisetech.ecalculo.zorbistore.ui.auth.register.body

data class RegisterBody(
    val address: String,
    val country: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val primaryContactNumber: String,
    val secondaryContactNumber: String,
    val shippingAddress: String
)