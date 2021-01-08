package com.raisetech.ecalculo.zorbistore.ui.main.home


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.*
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class HomeViewModel(
        private val getCategoriesRepository: CategoriesRepository,
        private val getProductRepository: ProductRepository,
        private val featuredProductRepository: FeaturedProductRepository,
        private val latestProductRepository: LatestProductRepository,
        private val onSaleRepository: OnSaleRepository,
        private val bannerRepository: BannerRepository
) : BaseViewModel() {

    var categoriesEvent = SingleLiveEvent<Unit>()
    var categoriesEventNavigation = SingleLiveEvent<Unit>()
    var categoryName: CategoriesResponse? = null
    var categoryNavigation:CategoriesResponse?=null
    var featuredProductEvent = SingleLiveEvent<Unit>()
    var latestProduct:NewProductResponseItem?=null
    var onSale:NewProductResponseItem?=null
    var latestProductEvent=SingleLiveEvent<Unit>()
    var productEvent = SingleLiveEvent<Unit>()
    var bannerEvent=SingleLiveEvent<Unit>()
    var categoryProductEvent = SingleLiveEvent<Unit>()
    var productName: List<com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data>? = null
    var featuredProductName: NewProductResponseItem? = null
    var banner:List<NewProductResponseItem>?=null
    var authListenerInfo: AuthListenerInfo? = null
    var onSaleProductEvent=SingleLiveEvent<Unit>()

    fun categories() {
        viewModelScope.launch {
            try {
                val response = getCategoriesRepository.getCategories()
                categoryName = response.body()
                categoriesEvent.call()
            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:NullPointerException){

            }catch (e:java.lang.NullPointerException){}
        }
    }


    fun getProduct() {

        viewModelScope.launch {
            try {
                val response = getProductRepository.getProduct()
                productName = response.body()
                productEvent.call()
            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }

//    fun getCategoriesProduct() {
//        viewModelScope.launch {
//            try {
//                val product = getProductRepository.getProduct()
//                val categories = getCategoriesRepository.getCategories()
//                productName = product.body()
//                categoryName = categories.body()
//                categoryProductEvent.call()
//            } catch (e: NoInternetException) {
//                authListenerInfo?.onWarning(e.message!!)
//            }catch (e: ApiException){
//
//            }catch (e:java.lang.NullPointerException){}
//        }
//
//    }

    fun categoriesNavigation(){

        viewModelScope.launch {
            try {
                val response = getCategoriesRepository.getCategories()
                categoryNavigation = response.body()
                categoriesEventNavigation.call()
            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }

    fun getFeaturedProduct() {

        viewModelScope.launch {
            try {

                val featuredProduct = featuredProductRepository.getProduct()
                featuredProductName = featuredProduct.body()
                featuredProductEvent.call()

            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }
    }


    fun getLatestProduct() {
        viewModelScope.launch {
            try {

                val latest = latestProductRepository.getProduct()
                latestProduct = latest.body()
               latestProductEvent.call()

            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }

    }


    fun getOnSaleProduct(){
        viewModelScope.launch {
            try {

                val onsaleProduct = onSaleRepository.getProduct()
                onSale = onsaleProduct.body()
                onSaleProductEvent.call()

            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }


    }


    fun getBanner(){
        viewModelScope.launch {
            try {

                var name = bannerRepository.getBanner()
                banner = name.body()
                bannerEvent.call()

            } catch (e: NoInternetException) {
                authListenerInfo?.onWarning(e.message!!)
            }catch (e: ApiException){

            }catch (e:java.lang.NullPointerException){}
        }

    }

}