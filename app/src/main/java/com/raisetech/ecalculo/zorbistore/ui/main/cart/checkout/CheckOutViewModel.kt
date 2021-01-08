package com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout


import android.content.Intent
import android.view.View
import androidx.lifecycle.viewModelScope
import com.raisetech.ecalculo.zorbistore.base.BaseViewModel
import com.raisetech.ecalculo.zorbistore.data.repo.OrderRepository
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.utils.ApiException
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.NoInternetException
import kotlinx.coroutines.launch

class CheckOutViewModel(private val orderRepository: OrderRepository) : BaseViewModel() {
    var authListenerInfo: AuthListenerInfo? = null


    var firstName: String? = null
    var lastName: String? = null
    var contactNumber: String? = null
    var email: String? = null
    var address1: String? = null
    var address2: String? = null


    fun submitOrder(view: View) {

        if (firstName.isNullOrEmpty()) {
            authListenerInfo?.onInfo("Please enter firstName")
        } else if (contactNumber.isNullOrEmpty()) {
            authListenerInfo?.onInfo("Please enter contact number")
        } else if (lastName.isNullOrEmpty()) {
            authListenerInfo?.onInfo("Please enter lastName")

        } else if (email.isNullOrEmpty()) {
            authListenerInfo?.onInfo("Please enter email")

        } else if (address1.isNullOrEmpty()) {
            authListenerInfo?.onInfo("Please enter addreess")

        } else if (address2.isNullOrEmpty()) {
            authListenerInfo?.onInfo("Please enter optional address")
        }

       else {

            viewModelScope.launch {

                try {
                    val orderResponse = lastName?.let {
                        email?.let { it1 ->
                            address1?.let { it2 ->
                                address2?.let { it3 ->
                                            orderRepository.order(
                                                firstName!!, it, contactNumber!!, it1, it2,
                                                it3
                                            )
                                        }
                                    }
                                }


                    }

                    if (orderResponse!!.isSuccessful) {
                      //  authListenerInfo?.onInfo("Your order number is" + orderResponse.body()?.id)
                        Intent(view.context, MainActivity::class.java).also {
                            view.context.startActivity(it)
                            ShoppingCart.clearCart()

                        }

                    } else {
                        authListenerInfo?.onInfo(orderResponse.message().toString())
                    }
                } catch (e: ApiException) {
                    authListenerInfo?.onWarning(e.message!!)
                } catch (e: NoInternetException) {
                    authListenerInfo?.onInfo(e.message!!)

                }catch (e:java.lang.NullPointerException){}
            }
        }

    }

}