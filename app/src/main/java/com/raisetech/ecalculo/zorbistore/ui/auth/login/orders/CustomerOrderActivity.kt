package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.constants.Constants
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.OrderDetailsActivity
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerOrderResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.Data
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerOrderActivity : BaseActivity<com.raisetech.ecalculo.databinding.ActivityCustomerOrderBinding, CustomerOrderViewModel>(), CustomerOrderAdapter.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.activity_customer_order
    override fun getViewModel(): CustomerOrderViewModel=customeOrderViewModel
    private val customeOrderViewModel:CustomerOrderViewModel by viewModel()


   lateinit var customerOrderAdapter: CustomerOrderAdapter
    var itemList = ArrayList<Data>()
    var product:String?=null
    var quantity:Int?=null
    var rate:String?=null
    var order:String?=null
    var orderHeaderId:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        with(viewDataBinding){
            with(customeOrderViewModel){

                customerOrderAdapter= CustomerOrderAdapter(this@CustomerOrderActivity,itemList)
                rvCustomerOrders.adapter=customerOrderAdapter

                getOrders(preferenceManager.getCustomerId())
                orderEvent.observe(this@CustomerOrderActivity, Observer {

                    orders?.data?.forEach { i->
                        val billAmount=i.billAmount
                        val ecommerceOrderDetails=i.ecommerceOrderDetailsList
                        val id=i.id
                        val nonTaxableAmount=i.nonTaxableAmount
                        val orderDate=i.orderDate
                        val orderNo=i.orderNo
                        val paymentMethod=i.paymentMethod
                        val status=i.status
                        val subTotalAmount=i.subTotalAmount

                        preferenceManager.setOrderId(id)

                        itemList.add(Data(billAmount,ecommerceOrderDetails,id,nonTaxableAmount,orderDate,orderNo,paymentMethod,status,subTotalAmount))
                    }

                    customerOrderAdapter.addAll(itemList)



                    progressBar6.visibility= View.GONE

                })

            }
        }
    }

    private fun initView() {
        with(viewDataBinding){

            ivBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onItemSelect(position: Int, itemList: Data) {

             product= itemList.paymentMethod
             rate=itemList.billAmount.toString()
             order=itemList.orderNo.toString()
            orderHeaderId=itemList.id


            var intent=Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra(Constants.Product,product)
            intent.putExtra(Constants.Quantity,quantity)
            intent.putExtra(Constants.Price,rate)
            intent.putExtra("order",order)
            intent.putExtra("order_header",orderHeaderId)
            startActivity(intent)



    }
}