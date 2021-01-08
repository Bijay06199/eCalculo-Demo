package com.raisetech.ecalculo.zorbistore.ui.main.home.featuredProduct


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.FeaturedProductRepository
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class FeaturedProductViewModel(private var featuredProductRepository: FeaturedProductRepository) : BaseViewModel(){

    var featuredProductEvent = SingleLiveEvent<Unit>()
    var productName: NewProductResponseItem? = null


    fun getFeaturedProduct() {

        viewModelScope.launch {
            try {

                val featuredProduct = featuredProductRepository.getProduct()
                productName = featuredProduct.body()
                featuredProductEvent.call()

            }  catch (e: NoInternetException) {
            }catch (e: ApiException){

            }
        }
    }
}