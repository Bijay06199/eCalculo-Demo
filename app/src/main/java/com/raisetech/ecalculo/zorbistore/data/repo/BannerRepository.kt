package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.network.SafeApiRequest
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import retrofit2.Response

class BannerRepository (var apiServices: zorbiApiServices): SafeApiRequest(){
    suspend fun getBanner(): Response<List<NewProductResponseItem>> {
        return apiServices.getBanner()
    }
}