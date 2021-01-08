package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerResponse
import retrofit2.Response

class CustomerDetailsRepository (var apiServices: zorbiApiServices){
    suspend fun getCustomer(email: String?): Response<List<CustomerResponse>> {
        return apiServices.getCustomerDetails(email)
    }
}