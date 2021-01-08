package com.raisetech.ecalculo.zorbistore.ui.main.home.onSale.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.product_items_recyclerview.view.*

class OnInnerSaleAdapter(private val context: Context, private val listener:OnItemClickListener, var cartItems:List<Data>): RecyclerView.Adapter<OnInnerSaleAdapter.ViewHolder>(){


    var count: Int = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnInnerSaleAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var images=cartItems[position].images
        var name=cartItems[position].name

        if (!name.equals("Banner Test 1", ignoreCase = true) && !name.equals(
                "Banner Test 3", ignoreCase = true) && !name.equals("Slider Banner 2",ignoreCase = true)
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




            holder.bindProduct(cartItems[position])
        } else {
            holder.root.visibility = View.GONE
            holder.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            holder.root.removeView(holder.root)



        }



        holder.root.setOnClickListener {
            listener.onItemSaleSelect(holder.adapterPosition, cartItems[holder.adapterPosition])


        }



    }


    inner class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {
        val productName = containerView.tv_product_name
        val productImage = containerView.iv_item
        val productPrice = containerView.tv_price
        val root = containerView.root




        @SuppressLint("CheckResult")
        fun bindProduct(cartItem: Data) {


            Observable.create(ObservableOnSubscribe<MutableList<CartItemModel>> {


                var cartItemModel= CartItemModel(cartItem,0)

                var _counter=cartItemModel.quantity


                itemView.layout_add.setOnClickListener{view->

                    _counter++
                    listener.onSaleLayoutAddClick(adapterPosition, cartItems[adapterPosition])

                    val item= CartItemModel(cartItem)
                    ShoppingCart.addItem(item,itemView.context)


                    itemView.layout_add.visibility = View.GONE
                    itemView.cl_add_main.visibility = View.GONE
                    itemView.add_subtract_button.visibility = View.VISIBLE
                    ShoppingCart.getCart()
                    itemView.tv_cart_quantity.setText(Integer.toString(_counter))





                }

                itemView.cl_add_main.setOnClickListener {view ->

                    _counter++
                    val item= CartItemModel(cartItem)
                    ShoppingCart.addItem(item,itemView.context)


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

                notifyDataSetChanged()

            }


        }

    }

    interface OnItemClickListener {
        fun onItemSaleSelect(position: Int, itemList: Data)
        fun onSaleLayoutAddClick(position: Int, itemList: Data)
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