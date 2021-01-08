package com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.andrognito.flashbar.Flashbar
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.extentions.dangerFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.infoFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.successFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.warningFlashBar
import io.paperdb.Paper
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import kotlinx.android.synthetic.main.activity_check_out.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckOutActivity : BaseActivity<com.raisetech.ecalculo.databinding.ActivityCheckOutBinding, CheckOutViewModel>(), AuthListenerInfo {
    override fun getLayoutId(): Int = R.layout.activity_check_out
    override fun getViewModel(): CheckOutViewModel =checkOutViewModel
    private val checkOutViewModel:CheckOutViewModel by viewModel()


    var flashbar:Flashbar?=null
    var totalPrice:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         totalPrice=intent.getStringExtra("totalPrice")
         initView()
    }

    private fun initView() {

        with(viewDataBinding){
            eTFirstName.setText(preferenceManager.getFirstName())
            eTLastName.setText(preferenceManager.getLastName())
            eTMailAddress.setText(preferenceManager.getEmail())
            eTContactNumber.setText(preferenceManager.getNumber())
            eTAddress1.setText(preferenceManager.getAddress())
            eTAddress2.setText(preferenceManager.getAddress())

            tVItemCount.setText(ShoppingCart.getCart().size.toString())




            var totalPrice =ShoppingCart.getCart()


                .fold(0.toDouble()) { acc, cartItemModel ->
                    if(cartItemModel.product.price==0.toDouble()){
                        return
                    }else{
                        acc + cartItemModel.quantity.times(
                            cartItemModel.product.price!!.toDouble()
                        )
                    }

                }
            tVTotalAmt.text="RS ${totalPrice}"


        }





        checkOutViewModel.authListenerInfo=this

    }


    override fun onSuccess(message: String) {

        flashbar=successFlashBar(message)
        flashbar?.show()

    }

    override fun onStarted() {


    }

    override fun onInfo(message: String) {
        flashbar=infoFlashBar(message)
        flashbar?.show()

    }

    override fun onWarning(message: String) {
        flashbar=warningFlashBar(message)
        flashbar?.show()

    }

    override fun onDanger(message: String) {
        flashbar=dangerFlashBar(message)
        flashbar?.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}