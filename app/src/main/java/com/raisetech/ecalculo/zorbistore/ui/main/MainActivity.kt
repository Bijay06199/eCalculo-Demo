package com.raisetech.ecalculo.zorbistore.ui.main


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.library.BuildConfig
import com.google.android.gms.common.internal.Constants
import com.google.android.material.badge.BadgeDrawable
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.ui.auth.login.LoginFragment
import com.raisetech.ecalculo.zorbistore.ui.main.cart.CartFragment
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.CategoriesFragment
import com.raisetech.ecalculo.zorbistore.ui.main.home.HomeFragment
import com.raisetech.ecalculo.zorbistore.ui.main.home.adapter.LatestProductAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.ShopFragment
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity() : BaseActivity<com.raisetech.ecalculo.databinding.ActivityMainBinding, MainViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_main
    override fun getViewModel(): MainViewModel =mainViewModel
    private  val mainViewModel:MainViewModel by viewModel()




   lateinit var badge:BadgeDrawable
    lateinit var latestProductAdapter: LatestProductAdapter
    var itemList=ArrayList<NewProductResponseItem>()
    var itemModel=ArrayList<CartItemModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        badge=viewDataBinding.bottomNavigation.getOrCreateBadge(R.id.page_3)
        badge.isVisible=true
       badge.number= ShoppingCart.getCart().size



        initView()

        bottomNavigationItem()

        if(savedInstanceState==null){

                HomeFragment.start(this,R.id.main_screen_container)

        }

    }



    private fun initView() {
        with(viewDataBinding){


            fabButton.setOnClickListener {
                ShopFragment.start(this@MainActivity,R.id.main_screen_container)
            }
        }
    }

    fun isOnline(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null &&
                cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

    private fun bottomNavigationItem() {
        with(viewDataBinding){

            bottomNavigation.setOnNavigationItemSelectedListener { item ->

                when(item.itemId){
                    R.id.page_1->{
                        HomeFragment.start(this@MainActivity,R.id.main_screen_container)
                        true
                    }
                    R.id.page_2->{
                        CategoriesFragment.start(this@MainActivity,R.id.main_screen_container)
                        true
                    }
                    R.id.page_3->{
                        CartFragment.start(this@MainActivity,R.id.main_screen_container)
                        true
                    }


                    R.id.page_4->{
                        LoginFragment.start(this@MainActivity,R.id.main_screen_container)
                        true
                    }
                    else -> false
                }

            }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (BuildConfig.DEBUG && data == null) {
                error("Assertion failed")

            }



        }


    }







}