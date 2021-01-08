package com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail

import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.SubItemRepository
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CategoriesSubItemViewModel(private val subItemRepository: SubItemRepository):BaseViewModel() {

    var categoryWise: NewProductResponseItem?=null
    var categoryWiseEvent= SingleLiveEvent<Unit>()


    fun getCategoryWise( id:Int?) {

        viewModelScope.launch {
            try {
                val response = subItemRepository.getSubcategory(id!!)
                categoryWise = response.body()!!
                categoryWiseEvent.call()
            } catch (e: NoInternetException){

            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }
}