package com.raisetech.ecalculo.zorbistore.ui.main.cart.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.data.prefs.PreferenceManager
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.CartFragment
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.body.EcommerceOrderDetails
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.view.tv_product_name
import kotlinx.android.synthetic.main.recyclerview_cartitems.view.*
import kotlinx.android.synthetic.main.recyclerview_cartitems.view.tv_price

class ShoppingCartAdapter(
        var context: Context,
        var cartItems: List<CartItemModel>,
        var preferenceManager: PreferenceManager,
        private val listener: OnItemClickListener,
        var fragment: CartFragment

    ) :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    var productId:Int?=null
    var quantity:Int?=null
    var orderDetails:List<EcommerceOrderDetails>?=null


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingCartAdapter.ViewHolder {


        val layout =
            LayoutInflater.from(context).inflate(R.layout.recyclerview_cartitems, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: ShoppingCartAdapter.ViewHolder, position: Int) {




//        for (i in 0 until  ShoppingCart.getCart().size) {
//            ShoppingCart.getCart()
//
//           productId = cartItems.get(i).product.id
//           quantity = cartItems.get(i).quantity
//
//        }
//        preferenceManager.setProuductId(productId!!)
//        preferenceManager.seQuantity(quantity!!)
//
//     //   preferenceManager.setOrderDetails(listOf(LineItem(productId,quantity,null)))
////        preferenceManager.seQuantity(quantity!!)
////
//        orderDetails= listOf(LineItem(productId,quantity,null))





        viewHolder.main.setOnClickListener {
            listener.onSelectListener(
                viewHolder.adapterPosition,
                cartItems[viewHolder.adapterPosition]
            )
        }
        viewHolder.bindItem(cartItems[position])
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var main = view.main

        @SuppressLint("CheckResult")
        fun bindItem(cartItem: CartItemModel) {

            var images = cartItem.product.images






            for (i in 0 until images!!.size) {
                var imageUrl = images[i]?.src


                Glide.with(itemView.iv_product)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_empty_image)
                    .into(itemView.iv_product)


            }

            Observable.create(ObservableOnSubscribe<MutableList<CartItemModel>> {


                var _counter = cartItem.quantity

                itemView.cl_add_cart.setOnClickListener { view ->


                    _counter++
                    val item = CartItemModel(cartItem.product)
                    ShoppingCart.addItem(item, itemView.context)
                    ShoppingCart.getCart()
                    (itemView.context as MainActivity).badge.number = ShoppingCart.getCart().size
                    itemView.tv_total_cart.setText(Integer.toString(_counter))
                    Paper.init(itemView.context)
                    itemView.findFragment<CartFragment>().no_of_items.text=ShoppingCart.getCart().size.toString()
                    itemView.findFragment<CartFragment>().tv_total_price.text= cartItem.quantity.times(cartItem.product.price!!.toDouble()).toString()


                }

                itemView.cl_subtract_cart.setOnClickListener { view ->

                    if (_counter > 1) {
                        _counter--

                        val item = CartItemModel(cartItem.product)
                        ShoppingCart.removeItem(item, itemView.context)
                        ShoppingCart.getCart()
                        (itemView.context as MainActivity).badge.number =
                            ShoppingCart.getCart().size
                        Paper.init(itemView.context)
                        itemView.findFragment<CartFragment>().no_of_items.text=ShoppingCart.getCart().size.toString()
                        itemView.findFragment<CartFragment>().tv_total_price.text= cartItem.quantity.times(cartItem.product.price!!.toDouble()).toString()
                        itemView.tv_total_cart.setText(Integer.toString(_counter))


                    } else if (_counter == 1) {
                        val item = CartItemModel(cartItem.product)
                        itemView.main.visibility = View.GONE
                        ShoppingCart.removeItem(item, itemView.context)
                        (itemView.context as MainActivity).badge.number =
                            ShoppingCart.getCart().size
                        Paper.init(itemView.context)
                        itemView.findFragment<CartFragment>().no_of_items.text=ShoppingCart.getCart().size.toString()
                        itemView.findFragment<CartFragment>().tv_total_price.text= cartItem.quantity.times(cartItem.product.price!!.toDouble()).toString()


                    }


                    // ShoppingCart.getCart()


                }


            }).subscribe { cartItem ->
                var quantity = 0

                cartItem.forEach { cartItem ->
                    quantity += cartItem.quantity
                }

                itemView.tv_total_cart.setText(quantity)

            }




            itemView.tv_product_name.text = cartItem.product.name

            itemView.tv_price.text = "${cartItem.product.price}"

            itemView.tv_total_cart.text = cartItem.quantity.toString()

        }


    }

    interface OnItemClickListener {
        fun onSelectListener(position: Int, itemList: CartItemModel)

    }

}