package com.raisetech.ecalculo.zorbistore.ui.main.shop.response

data class Data(
        var id:Int,
        var description:String?,
    val categories: List<Category>,
    val downloadable: Boolean,
    val images: List<Image>,
    val in_stock: Boolean,
    val name: String,
    val on_sale: Boolean,
    val price: Double,
    val shipping_required: Boolean,
    val shipping_taxable: Boolean,
    val title: String
)