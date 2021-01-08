package com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.model.OrderDetailsModel
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.response.DataXX
import kotlinx.android.synthetic.main.recyclerview_order_details.view.*

class OrderDetailsAdapter (var itemList:List<DataXX>):RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>(){



    inner class ViewHolder(val containerView: View):RecyclerView.ViewHolder(containerView){

        var product=containerView.product
        var quantity=containerView.quantity
        var rate=containerView.rate
        var total=containerView.quantity_rate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_order_details,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.product.setText(itemList[position].itemName)
        holder.quantity.setText(itemList[position].quantity.toString())
        holder.rate.setText(itemList[position].rate.toString())
        holder.total.setText(itemList[position].itemCode)

    }


    
    fun addAll(itemList: ArrayList<DataXX>){
        this.itemList=itemList
        notifyDataSetChanged()
        
    }


}