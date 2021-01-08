package com.raisetech.ecalculo.zorbistore.ui.main.categories.response

data class CategoriesResponse(
    val `data`: List<Data>,
    val message: String,
    val status: String
)