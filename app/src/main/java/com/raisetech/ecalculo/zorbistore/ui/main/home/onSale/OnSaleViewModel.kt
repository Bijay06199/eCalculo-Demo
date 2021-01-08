package com.raisetech.ecalculo.zorbistore.ui.main.home.onSale


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.OnSaleRepository
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class OnSaleViewModel(private var onSaleRepository: OnSaleRepository) : BaseViewModel(){

    var onSale:NewProductResponseItem?=null
    var onSaleProductEvent= SingleLiveEvent<Unit>()


    fun getOnSaleProduct(){
        viewModelScope.launch {
            try {

                val onsaleProduct = onSaleRepository.getProduct()
                onSale = onsaleProduct.body()
                onSaleProductEvent.call()

            } catch (e: NoInternetException) {
            }catch (e: ApiException){

            }
        }


    }
}