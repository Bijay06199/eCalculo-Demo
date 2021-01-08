package com.raisetech.ecalculo.zorbistore.di


import com.raisetech.ecalculo.zorbistore.ui.auth.login.EditProfileViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.login.LoginViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.login.ZorbistoreViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.CustomerOrderViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.OrderDetailsViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.register.RegisterFinalViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.register.RegisterNameViewModel
import com.raisetech.ecalculo.zorbistore.ui.auth.register.RegisterPasswordViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.MainViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.cart.CartViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.CheckOutViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.CategoriesViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail.CategoriesDetailViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail.CategoriesItemViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail.CategoriesSubItemViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.contact.ContactViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.home.HomeViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.home.featuredProduct.FeaturedProductViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.home.latestProducts.LatestProductViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.home.onSale.OnSaleViewModel
import com.raisetech.ecalculo.zorbistore.ui.main.shop.ShopViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule:Module= module {


    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(),get(),get(),get(),get(),get()) }
    viewModel { ContactViewModel() }
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { CategoriesViewModel(get()) }
    viewModel { RegisterNameViewModel(get()) }
    viewModel { RegisterPasswordViewModel(get()) }
    viewModel { RegisterFinalViewModel(get()) }
    viewModel { CategoriesDetailViewModel(get(),get()) }
    viewModel { CategoriesItemViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { ShopViewModel(get(),get()) }
    viewModel { CartViewModel() }
    viewModel { CheckOutViewModel(get()) }
    viewModel { LatestProductViewModel(get()) }
    viewModel { OnSaleViewModel(get()) }
    viewModel { FeaturedProductViewModel(get()) }
    viewModel { CustomerOrderViewModel(get()) }
    viewModel { OrderDetailsViewModel(get()) }
    viewModel { ZorbistoreViewModel(get()) }
    viewModel { CategoriesSubItemViewModel(get()) }
}