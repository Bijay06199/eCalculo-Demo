package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import retrofit2.Response

class ProductRepository (var apiServices: zorbiApiServices){
    suspend fun getProduct():Response<List<Data>>{
        return apiServices.getProduct()
    }
}