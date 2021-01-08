package com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.CategoriesRepository
import com.raisetech.ecalculo.zorbistore.data.repo.SubCategoryRepository
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.SubCategoryResponse
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CategoriesDetailViewModel(private val getCategoriesRepository: CategoriesRepository,private val subCategoryRepository: SubCategoryRepository) : BaseViewModel(){

    var categoriesEvent= SingleLiveEvent<Unit>()
    var categoryName:CategoriesResponse?=null
    var subCategory:SubCategoryResponse?=null
    var subCategoryEvent=SingleLiveEvent<Unit>()


    fun categories(){
        viewModelScope.launch {
            try {
                val response= getCategoriesRepository.getCategories()
                categoryName=response.body()
                categoriesEvent.call()
            }
            catch (e: NoInternetException){

            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }

    fun subCategories(){
        viewModelScope.launch {
            try {
                val response=subCategoryRepository.getSubcategory()
                subCategory=response.body()
                subCategoryEvent.call()
            }catch (e: NoInternetException){

            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }
}