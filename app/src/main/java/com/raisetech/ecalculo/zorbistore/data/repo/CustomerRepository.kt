package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.body.LoginBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerResponse
import retrofit2.Response

class CustomerRepository (var apiServices: zorbiApiServices){
    suspend fun getCustomer(email:String,password:String): Response<CustomerResponse> {

        val requestData=LoginBody(email,password)
        return apiServices.getCustomer(requestData)
    }
}