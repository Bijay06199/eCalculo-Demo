package com.raisetech.ecalculo.zorbistore.ui.main.home.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.product_items_recyclerview.view.*
import kotlinx.android.synthetic.main.product_items_recyclerview.view.add_subtract_button
import kotlinx.android.synthetic.main.product_items_recyclerview.view.cl_add
import kotlinx.android.synthetic.main.product_items_recyclerview.view.cl_subtract
import kotlinx.android.synthetic.main.product_items_recyclerview.view.root
import kotlinx.android.synthetic.main.product_items_recyclerview.view.tv_cart_quantity
import kotlinx.android.synthetic.main.product_items_recyclerview.view.tv_price
import kotlinx.android.synthetic.main.product_items_recyclerview.view.tv_product_name

class LatestProductAdapter (var context:Context,private val listener:OnItemClickListener, var cartItems: List<Data>):
    RecyclerView.Adapter<LatestProductAdapter.ViewHolder>() {

    var count: Int = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var images=cartItems[position].images
        var name=cartItems[position].name

        if (!name.equals("Banner Test", ignoreCase = true) && !name.equals(
                "Banner Test 2", ignoreCase = true) && !name.equals("Banner Test 1",ignoreCase = true)&& !name.equals("Banner Test 3",ignoreCase = true)
        ){


            for(i in 0 until images!!.size){
                var imageUrl = images[i]?.src


                Glide.with(holder.productImage)
                    .load(imageUrl)
                    .placeholder(R.drawable.place_holder_icon)
                    .into(holder.productImage)
            }

            holder.productName.text = cartItems[position].name
            holder.productPrice.text = cartItems[position].price.toString()

            var salePrice=cartItems[position].price
            var regularPrice=cartItems[position].price
            holder.regularPrice.setText(regularPrice.toString())

//            if (salePrice==""){
//                holder.regularPrice.setText("")
//            }
//            else {
//                holder.regularPrice.setText(regularPrice)
//                holder.regularPrice.setPaintFlags(holder.regularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
//            }


            holder.bindProduct(cartItems[position])





        } else {
            holder.root.visibility = View.GONE
            holder.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            holder.root.removeView(holder.root)



        }



        holder.root.setOnClickListener {
            listener.onItemLatestSelect(holder.adapterPosition, cartItems[holder.adapterPosition])
        }




    }


    inner class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {
        val productName = containerView.tv_product_name
        val productImage = containerView.iv_item
        val productPrice = containerView.tv_price
        val root = containerView.root
        val layoutAdd = containerView.layout_add
        val subtract = containerView.cl_subtract
        val add = containerView.cl_add
        val addSubtract = containerView.add_subtract_button
        val addMain = containerView.cl_add_main
        val cart_quantity = containerView.tv_cart_quantity
        val regularPrice=containerView.tv_regular_price



        @SuppressLint("CheckResult")
        fun bindProduct(cartItem: Data) {


            Observable.create(ObservableOnSubscribe<MutableList<CartItemModel>> {


                var cartItemModel=CartItemModel(cartItem,0)

                   var _counter=cartItemModel.quantity


                    itemView.layout_add.setOnClickListener{view->

                        _counter++
                        listener.onLayoutAddLatestClick(adapterPosition, cartItems[adapterPosition])

                        val item= CartItemModel(cartItem)
                        ShoppingCart.addItem(item,itemView.context)


                        itemView.layout_add.visibility = View.GONE
                        itemView.cl_add_main.visibility = View.GONE
                        itemView.add_subtract_button.visibility = View.VISIBLE
                        ShoppingCart.getCart()
                        (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size
                        itemView.tv_cart_quantity.setText(Integer.toString(_counter))





                    }

                    itemView.cl_add_main.setOnClickListener {view ->

                        _counter++
                        val item= CartItemModel(cartItem)
                        ShoppingCart.addItem(item,itemView.context)

                        (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size

                        itemView.layout_add.visibility = View.GONE
                        itemView.cl_add_main.visibility = View.GONE
                        itemView.add_subtract_button.visibility = View.VISIBLE
                        ShoppingCart.getCart()
                        itemView.tv_cart_quantity.setText(Integer.toString(_counter))

                    }

                    itemView.cl_add.setOnClickListener {view->

                        _counter++
                        val item= CartItemModel(cartItem)
                        ShoppingCart.addItem(item,itemView.context)
                        (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size


                        itemView.layout_add.visibility = View.GONE
                        itemView.cl_add_main.visibility = View.GONE
                        itemView.add_subtract_button.visibility = View.VISIBLE
                        ShoppingCart.getCart()
                        itemView.tv_cart_quantity.setText(Integer.toString(_counter))




                    }

                    itemView.cl_subtract.setOnClickListener {

                        _counter--
                        val item= CartItemModel(cartItem)
                        ShoppingCart.removeItem(item,itemView.context)
                        ShoppingCart.getCart()
                        itemView.tv_cart_quantity.setText(Integer.toString(_counter))
                        (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size

                        if (_counter==0){

                            ShoppingCart.removeItem(item,itemView.context)
                            itemView.layout_add.visibility = View.VISIBLE
                            itemView.cl_add_main.visibility = View.VISIBLE
                            itemView.add_subtract_button.visibility = View.GONE

                        }




                    }





            }).subscribe{
                    cartItem->
                var quantity=0
                var productId=0

                cartItem.forEach { cartItem->
                    quantity += cartItem.quantity

                }


                itemView.tv_cart_quantity.setText(quantity)
                (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size

                notifyDataSetChanged()

            }



//
//            itemView.tv_product_name.text = cartItem.product.name
//
//            itemView.tv_price.text = "$${cartItem.product.price}"

//            itemView.tv_total_cart.text = cartItem.quantity.toString()

        }

    }

    interface OnItemClickListener {
        fun onItemLatestSelect(position: Int, itemList: Data)
        fun onLayoutAddLatestClick(position: Int, itemList: Data)
//        fun onAddLatestClick(position: Int, itemList: NewProductResponseItem)
//        fun onSubtractLatestClick(position: Int, itemList: NewProductResponseItem)
    }

    fun filteredList(filteredNames:ArrayList<Data>){

        this.cartItems=filteredNames
        notifyDataSetChanged()

    }



    fun addAll(itemList: List<Data>){
        this.cartItems=itemList
        notifyDataSetChanged()
    }




}


