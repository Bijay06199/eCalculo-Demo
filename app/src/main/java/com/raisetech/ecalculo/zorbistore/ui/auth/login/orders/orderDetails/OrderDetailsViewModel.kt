package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails


import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.OrderDetailsRepository
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.response.DataXX
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.response.OrderDetailsResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerOrderResponse
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import com.raisetech.ecalculo.zorbistore.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class OrderDetailsViewModel(val orderDetailsRepository: OrderDetailsRepository) : BaseViewModel(){

    var orderDetails:OrderDetailsResponse?=null
    var orderDetailsEvent=SingleLiveEvent<Unit>()


            fun orderDetails(id:Int){

                viewModelScope.launch {
                    try {
                      var response=orderDetailsRepository.getOrderDetails(id)
                        orderDetails=response.body()
                        orderDetailsEvent.call()

                    }catch (e:NoInternetException){

                    }catch (e:ApiException){

                    }catch (e:NullPointerException){

                    }
                }
            }

}