package com.raisetech.ecalculo.zorbistore.data.repo

import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.response.OrderDetailsResponse
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import retrofit2.Response

class OrderDetailsRepository (var apiServices: zorbiApiServices){
    suspend fun getOrderDetails(id:Int): Response<OrderDetailsResponse> {
        return apiServices.getOrderDetails(id)
    }
}