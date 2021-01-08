package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerOrderResponse
import retrofit2.Response

class CustomerOrderRepository (var apiServices: zorbiApiServices){
    suspend fun getCustomerOrder(id: Int?): Response<CustomerOrderResponse> {
        return apiServices.getCustomersOrder(id)
    }
}