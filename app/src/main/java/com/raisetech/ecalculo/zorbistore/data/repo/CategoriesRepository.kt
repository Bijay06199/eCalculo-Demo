package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.Data
import retrofit2.Response

class CategoriesRepository (var apiServices: zorbiApiServices){
    suspend fun getCategories(): Response<CategoriesResponse> {
        return apiServices.getCategories()
    }
}