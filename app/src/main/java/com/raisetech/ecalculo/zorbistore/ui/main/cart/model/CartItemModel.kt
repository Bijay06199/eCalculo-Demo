package com.raisetech.ecalculo.zorbistore.ui.main.cart.model

import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem


data class CartItemModel(var product: Data, var quantity:Int=0)