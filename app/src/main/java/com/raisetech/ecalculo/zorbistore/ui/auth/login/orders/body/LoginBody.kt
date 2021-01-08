package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.body

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName

data class LoginBody (
        @SerializedName("email")
        val email:String?,
        @SerializedName("password")
        val password:String?
        )