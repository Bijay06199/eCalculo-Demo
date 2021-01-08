package com.raisetech.ecalculo.zorbistore.ui.main.home.latestProducts


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.LatestProductRepository
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LatestProductViewModel(var latestProductRepository: LatestProductRepository) : BaseViewModel(){

    var latestProduct:NewProductResponseItem?=null
    var latestProductEvent= SingleLiveEvent<Unit>()


    fun getLatestProduct() {
        viewModelScope.launch {
            try {

                val latest = latestProductRepository.getProduct()
                latestProduct = latest.body()
                latestProductEvent.call()

            } catch (e: NoInternetException) {
            }catch (e: ApiException){

            }
        }

    }
}