package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails


import android.os.Bundle
import androidx.lifecycle.Observer
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.constants.Constants
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.adapter.OrderDetailsAdapter
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.model.OrderDetailsModel
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.response.DataXX
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderDetailsActivity : BaseActivity<com.raisetech.ecalculo.databinding.ActivityOrderDetailsBinding, OrderDetailsViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_order_details
    override fun getViewModel(): OrderDetailsViewModel = orderDetailsViewModel
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModel()

    var productName: String? = null
    var quantityT: Int? = null
    var rateT: String? = null
    var order: String? = null
    var orderHeaderId: Int = 0

    lateinit var orderDetailsAdapter: OrderDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productName = intent.getStringExtra(Constants.Product)
        quantityT = intent.getIntExtra(Constants.Quantity, 0)
        rateT = intent.getStringExtra(Constants.Price)
        order = intent.getStringExtra("order")
        orderHeaderId = intent.getIntExtra("order_header", 0)
        initView()

    }

    private fun initView() {
        with(viewDataBinding) {
            with(orderDetailsViewModel) {


                ivBack.setOnClickListener {
                    finish()
                }

                var total = quantityT?.times(rateT!!.toDouble())
                var itemList = ArrayList<DataXX>()

                orderDetails(orderHeaderId)

                orderDetailsAdapter = OrderDetailsAdapter(itemList)


                orderDetailsEvent.observe(this@OrderDetailsActivity, Observer {
                    recyclerviewOrderDetails.adapter = orderDetailsAdapter
                    orderDetails?.data?.forEach { i ->
                        var id = i.id
                        var rate = i.rate
                        var quantity = i.quantity
                        var itemName = i.itemName
                        var itemCode = i.itemCode

                        itemList.add(DataXX(id, itemCode, itemName, quantity, rate))
                    }
                    orderDetailsAdapter.addAll(itemList)


                })



                tvTotalPrice.setText(rateT)


            }


        }
    }
}