package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import retrofit2.Response

class CategoriesWiseRepository (var apiServices: zorbiApiServices){
    suspend fun getCategoriesWise(id: Int?): Response<NewProductResponseItem> {
        return apiServices.getProductCategoryWise(id,100)
    }
}