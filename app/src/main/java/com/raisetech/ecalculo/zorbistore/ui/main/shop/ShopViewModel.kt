package com.raisetech.ecalculo.zorbistore.ui.main.shop


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.ProductRepository
import com.raisetech.ecalculo.zorbistore.data.repo.ShoppingRepository
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class ShopViewModel(private val productRepository: ProductRepository, private val shoppingRepository: ShoppingRepository) : BaseViewModel(){

    var productEvent= SingleLiveEvent<Unit>()
    var productName: NewProductResponseItem?=null


    fun getProduct(page:Int,per_page:Int){

        viewModelScope.launch {
            try{
                val response=shoppingRepository.getProduct(page,per_page)
                productName=response.body()
                productEvent.call()
            }
            catch (e: NoInternetException) {
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }

}