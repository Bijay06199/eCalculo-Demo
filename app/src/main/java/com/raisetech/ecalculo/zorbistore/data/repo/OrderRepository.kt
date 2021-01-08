package com.raisetech.ecalculo.zorbistore.data.repo


import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.network.SafeApiRequest
import com.raisetech.ecalculo.zorbistore.data.prefs.PreferenceManager
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.body.*
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.response.OrderResponse
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import org.json.JSONException
import retrofit2.Response

class OrderRepository(var apiServices: zorbiApiServices, var preferenceManager: PreferenceManager) :
    SafeApiRequest() {





    suspend fun order(
        firstName: String,
        lastName: String,
        contactNumber: String,
        email: String,
        address1: String,
        address2: String
    ): Response<OrderResponse>? {

        var totalPrice =ShoppingCart.getCart()


                .fold(0.toDouble()) { acc, cartItemModel ->
                    if(cartItemModel.product.price==0.toDouble()){
                      return null
                    }else{
                        acc + cartItemModel.quantity.times(
                                cartItemModel.product.price!!.toDouble()
                        )
                    }

                }

        val requestData = OrderBody(
                 totalPrice,
               preferenceManager.getCustomerId(),
                null,
                getCartItemsFromDB(),
                 totalPrice,
                "COD",
                 totalPrice,
                null,
                null,
                null
        )

        return apiServices.order(requestData)
    }

    private fun getCartItemsFromDB(): ArrayList<EcommerceOrderDetails>? {

        val cartDTOList: List<CartItemModel> = ShoppingCart.getCart()
        val jsonArray = ArrayList<EcommerceOrderDetails>()
        if (cartDTOList != null && cartDTOList.size > 0) {
            for (i in cartDTOList.indices) {
                try {
                    jsonArray.add( EcommerceOrderDetails(cartDTOList[i].product.id.toString(),cartDTOList[i].quantity.toString(),cartDTOList[i].product.price.toString()))

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }
        return jsonArray
    }



}