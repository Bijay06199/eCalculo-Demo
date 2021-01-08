package com.raisetech.ecalculo.zorbistore.data.repo

import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.network.SafeApiRequest
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.SubCategoryResponse
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import retrofit2.Response

class SubItemRepository (var apiServices: zorbiApiServices): SafeApiRequest(){
    suspend fun getSubcategory(id:Int): Response<NewProductResponseItem> {
        return apiServices.getSubCategory(id)
    }
}