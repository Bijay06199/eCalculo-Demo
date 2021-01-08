package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.CustomerOrderRepository
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerOrderResponse
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CustomerOrderViewModel(var customerOrderRepository: CustomerOrderRepository) : BaseViewModel(){

    var orderEvent= SingleLiveEvent<Unit>()
    var orders:CustomerOrderResponse?=null

    fun getOrders(id:Int?){

        viewModelScope.launch {
            try {
                val response = customerOrderRepository.getCustomerOrder(id)
                orders = response.body()!!
                orderEvent.call()
            } catch (e: NoInternetException){

            }catch (e: ApiException){

            }
        }


    }


}