package com.raisetech.ecalculo.zorbistore.ui.main.home.adapter


import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.product_items_recyclerview.view.*

class OnSaleProductAdapter (private val listener:OnItemClickListener,var itemList:List<Data>):
    RecyclerView.Adapter<OnSaleProductAdapter.ViewHolder>() {

    var count: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnSaleProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var images=itemList[position].images

        for(i in 0 until images!!.size){
            var imageUrl = images[i]?.src


            Glide.with(holder.productImage)
                .load(imageUrl)
                .placeholder(R.drawable.place_holder_icon)
                .into(holder.productImage)
        }

        holder.productName.text = itemList[position].name
        holder.productPrice.text = itemList[position].price.toString()

        var salePrice=itemList[position].price
        var regularPrice=itemList[position].price
        holder.regularPrice.setText(regularPrice.toString())

//        if (salePrice==""){
//            holder.regularPrice.setText("")
//        }
//        else {
//            holder.regularPrice.setText(regularPrice)
//            holder.regularPrice.setPaintFlags(holder.regularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
//        }

        holder.bindProduct(itemList[position])


        holder.root.setOnClickListener {
            listener.onItemSaleSelect(holder.adapterPosition, itemList[holder.adapterPosition])
//            listener.onSaleLayoutAddClick(holder.adapterPosition, itemList[holder.adapterPosition])
//            listener.onSaleAddClick(holder.adapterPosition, itemList[holder.adapterPosition])
//            listener.onSaleSubtractClick(holder.adapterPosition, itemList[holder.adapterPosition])

        }

//        holder.layoutAdd.setOnClickListener {
//            holder.layoutAdd.visibility = View.GONE
//            holder.addMain.visibility = View.GONE
//            holder.addSubtract.visibility = View.VISIBLE
//            holder.cart_quantity.setText("" + count++)
//
//
//        }
//
//        holder.addMain.setOnClickListener {
//
//            holder.layoutAdd.visibility = View.GONE
//            holder.addMain.visibility = View.GONE
//            holder.addSubtract.visibility = View.VISIBLE
//            holder.cart_quantity.setText("" + count++)
//
//        }
//
//        holder.add.setOnClickListener {
//
//            holder.layoutAdd.visibility = View.GONE
//            holder.addMain.visibility = View.GONE
//            holder.subtract.visibility = View.VISIBLE
//            holder.cart_quantity.visibility = View.VISIBLE
//            holder.cart_quantity.setText("" + count++)
//
//        }
//
//        holder.subtract.setOnClickListener {
//
//            holder.cart_quantity.setText("" + count--)
//
//        }
//
//        if (holder.cart_quantity.text == "0") {
//            holder.layoutAdd.visibility = View.VISIBLE
//            holder.addMain.visibility = View.VISIBLE
//            holder.addSubtract.visibility = View.GONE
//
//        } else {
//            holder.layoutAdd.visibility = View.GONE
//            holder.addMain.visibility = View.GONE
//            holder.addSubtract.visibility = View.VISIBLE
//
//
//        }
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
        fun bindProduct(product: Data) {


            Observable.create(ObservableOnSubscribe<MutableList<CartItemModel>> {

                var cartItemModel=CartItemModel(product,0)

                var _counter=cartItemModel.quantity


                itemView.layout_add.setOnClickListener{view->

                    _counter++
                    listener.onSaleLayoutAddClick(adapterPosition, itemList[adapterPosition])



                    val item= CartItemModel(product)
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
                    val item= CartItemModel(product)
                    ShoppingCart.addItem(item,itemView.context)


                    itemView.layout_add.visibility = View.GONE
                    itemView.cl_add_main.visibility = View.GONE
                    itemView.add_subtract_button.visibility = View.VISIBLE
                    ShoppingCart.getCart()
                    (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size
                    itemView.tv_cart_quantity.setText(Integer.toString(_counter))

                }

                itemView.cl_add.setOnClickListener {view->

                    _counter++
                    val item= CartItemModel(product)
                    ShoppingCart.addItem(item,itemView.context)


                    itemView.layout_add.visibility = View.GONE
                    itemView.cl_add_main.visibility = View.GONE
                    itemView.add_subtract_button.visibility = View.VISIBLE
                    ShoppingCart.getCart()
                    (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size
                    itemView.tv_cart_quantity.setText(Integer.toString(_counter))




                }

                itemView.cl_subtract.setOnClickListener {

                    _counter--
                    val item= CartItemModel(product)
                    ShoppingCart.removeItem(item,itemView.context)
                    ShoppingCart.getCart()
                    (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size
                    itemView.tv_cart_quantity.setText(Integer.toString(_counter))

                    if (_counter==0){
                        ShoppingCart.removeItem(item,itemView.context)
                        itemView.layout_add.visibility = View.VISIBLE
                        itemView.cl_add_main.visibility = View.VISIBLE
                        (itemView.context as MainActivity).badge.number=ShoppingCart.getCart().size
                        itemView.add_subtract_button.visibility = View.GONE

                    }




                }


            }).subscribe{
                    cartItem->
                var quantity=0

                cartItem.forEach { cartItem->
                    quantity += cartItem.quantity
                }


                itemView.tv_cart_quantity.setText(quantity)

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
        fun onItemSaleSelect(position: Int, itemList: Data)
       fun onSaleLayoutAddClick(position: Int, itemList: Data)
//        fun onSaleAddClick(position: Int, itemList: NewProductResponseItem)
//        fun onSaleSubtractClick(position: Int, itemList: NewProductResponseItem)
    }

    fun filteredList(filteredNames:ArrayList<Data>){

        this.itemList=filteredNames
        notifyDataSetChanged()

    }

    fun addAll(itemList: List<Data>){
        this.itemList=itemList
        notifyDataSetChanged()
    }








}