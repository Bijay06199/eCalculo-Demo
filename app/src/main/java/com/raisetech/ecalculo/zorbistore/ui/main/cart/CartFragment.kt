package com.raisetech.ecalculo.zorbistore.ui.main.cart


import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrognito.flashbar.Flashbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseFragment
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.adapter.ShoppingCartAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.CheckOutActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.extentions.dangerFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.infoFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.successFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.warningFlashBar
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.persistent_bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.persistent_bottom_sheet.persistent_bottom_sheet


class CartFragment : BaseFragment<com.raisetech.ecalculo.databinding.FragmentCartBinding, CartViewModel>(), ShoppingCartAdapter.OnItemClickListener, AuthListenerInfo {

    override fun getLayoutId(): Int = R.layout.fragment_cart
    override fun getViewModel(): CartViewModel = cartViewModel
    private val cartViewModel: CartViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    lateinit var adapter: ShoppingCartAdapter
    var flashbar: Flashbar? = null
    var authListenerInfo:AuthListenerInfo?=null
    var cartItemModel: CartItemModel?=null
    var productId:Int?=null
    var quantity:Int?=null

    var totalPrice:Double=0.0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Paper.init(requireContext())
        bottomSheetBehavior = BottomSheetBehavior.from<MaterialCardView>(persistent_bottom_sheet)



        if (savedInstanceState == null) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        initView()
    //    setUpCartNumber()
        setUpRecyclerView()
    }

//    @SuppressLint("SetTextI18n")
//    private fun setUpCartNumber() {
//        with(viewDataBinding){
//
//
//            var count=1
//          Observable.create(ObservableOnSubscribe<MutableList<CartItemModel>> {
//
//              btnAddToCart.setOnClickListener{
//                  ShoppingCart.getCart()
//                  tvCartQuantity.text=ShoppingCart.getShoppingCartSize().toString()+1
//                  addSubtractButton.visibility = View.VISIBLE
//                  btnAddToCart.visibility = View.GONE
//
//              }
//
//              clAdd.setOnClickListener {
//                  ShoppingCart.getCart()
//                  tvCartQuantity.text=""+count++
//                   noOfItems.text=ShoppingCart.getCart().size.toString()
//              }
//
//              clSubtract.setOnClickListener {
//                  ShoppingCart.getCart()
//                  tvCartQuantity.text=""+count--
//                  noOfItems.text=ShoppingCart.getCart().size.toString()
//
//              }
//
//
//              if (tvCartQuantity.text == "0") {
//                  btnAddToCart.visibility = View.VISIBLE
//                  addSubtractButton.visibility = View.GONE
//                  noOfItems.text=ShoppingCart.getCart().size.toString()
//
//              }
//
//
//
//          }).subscribe { cartItem ->
//              var quantity = 0
//
//              cartItem.forEach { cartItem ->
//                  quantity += cartItem.quantity
//              }
//
//              tvCartQuantity.setText(quantity)
//
//          }
//
//
//        }
//    }

    private fun setUpRecyclerView() {
        with(viewDataBinding) {

            adapter = ShoppingCartAdapter(requireContext(), ShoppingCart.getCart(),preferenceManager,this@CartFragment,this@CartFragment)

            rvCartItem.adapter = adapter
            rvCartItem.layoutManager= LinearLayoutManager(this@CartFragment.activity)
            adapter.notifyDataSetChanged()

            if (ShoppingCart.getShoppingCartSize()==0){
                clEmpty.visibility=View.VISIBLE
            }

             totalPrice = ShoppingCart.getCart()




                .fold(0.toDouble()) { acc, cartItemModel ->
                    if(cartItemModel.product.price==0.toDouble()){
                        return
                    }else{
                        acc + cartItemModel.quantity.times(
                            cartItemModel.product.price!!.toDouble())



                    }

                }





            tvTotalPrice.text="${totalPrice}"

            noOfItems.text=ShoppingCart.getCart().size.toString()






        }
    }

    private fun initView() {
        with(viewDataBinding) {



            with(cartViewModel){
                cartViewModel.authListenerInfo = this@CartFragment

                btnCheckout.setOnClickListener {
                    if (noOfItems.text=="0"){
                        authListenerInfo?.onInfo("Cart is empty Cannot Proceed")
                    }
                    else
                    {

                        var intent=Intent(this@CartFragment.activity, CheckOutActivity::class.java)
                        intent.putExtra("totalPrice",totalPrice)
                        startActivity(intent)

                    }
                }

            }



            bottomSheetBehavior.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            amountDescription.visibility = View.VISIBLE
                            cvWithBottomSheet.visibility = View.GONE
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            amountDescription.visibility = View.GONE
                            cvWithBottomSheet.visibility = View.VISIBLE
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED.and(20) -> {
                            amountDescription.visibility = View.GONE
                            cvWithBottomSheet.visibility = View.VISIBLE
                        }
                    }
                }

            })


            topBar.setOnClickListener {
               startActivity(Intent(this@CartFragment.activity,MainActivity::class.java))
            }
        }
    }


    companion object {
        val TAG = CartFragment::class.java.simpleName
        fun start(activity: FragmentActivity, containerId: Int) {
            val fragment = CartFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(TAG)
                .commit()
        }
    }


    override fun onSelectListener(position: Int, itemList: CartItemModel) {

        var images = itemList.product.images

        var cartItemModel = CartItemModel(itemList.product, 0)

        var _counter = cartItemModel.quantity

       for (i in 0..position){

            with(viewDataBinding){

                btnAddToCart.setOnClickListener {

                    _counter++

                    val item = CartItemModel(itemList.product)
                    ShoppingCart.addItem(item, requireContext())
                    btnAddToCart.visibility = View.GONE
                    addSubtractButton.visibility = View.VISIBLE
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tv_total_price.text= itemList.quantity.times(itemList.product.price!!.toDouble()).toString()
                    tvCartQuantity.setText(Integer.toString(_counter))
                }


                clAdd.setOnClickListener { view ->

                    _counter++
                    val item = CartItemModel(itemList.product)
                    ShoppingCart.addItem(item, requireContext())

                    ShoppingCart.getCart()
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tv_total_price.text= itemList.quantity.times(itemList.product.price!!.toDouble()).toString()
                    tvCartQuantity.setText(Integer.toString(_counter))


                }


                clSubtract.setOnClickListener {

                    _counter--
                    val item = CartItemModel(itemList.product)
                    ShoppingCart.removeItem(item, requireContext())
                    ShoppingCart.getCart()
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tvCartQuantity.setText(Integer.toString(_counter))

                    if (_counter == 0) {
                        ShoppingCart.removeItem(item, requireContext())
                        btnAddToCart.visibility = View.VISIBLE
                        (context as MainActivity).badge.number=ShoppingCart.getCart().size
                        tv_total_price.text= itemList.quantity.times(itemList.product.price!!.toDouble()).toString()
                        addSubtractButton.visibility = View.GONE

                    }

                }
                btnAddToCart.visibility = View.VISIBLE
                addSubtractButton.visibility = View.GONE
            }





  }


        for (i in 0 until images!!.size) {

            var productImage = images[i]?.src
            setImageSrc(viewDataBinding.imageView5, productImage!!)
            var productName = itemList.product.name
         //   var description = itemList.product.description

            var price = itemList.product.price

            viewDataBinding.textView15.setText(productName)
            viewDataBinding.tvProductName.setText(productName)
         //   viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))
            viewDataBinding.textView17.setText(price.toString())
            viewDataBinding.tvTotalPrice.setText(price.toString())
            viewDataBinding.tvCartAmount1.setText(price.toString())
        }


        if (bottomSheetBehavior.state!=BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_HALF_EXPANDED.and(20)


        }
        else{
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED
        }
    }


    override fun onSuccess(message: String) {
        flashbar = successFlashBar(message)
        flashbar?.show()
    }

    override fun onStarted() {

    }

    override fun onInfo(message: String) {
        flashbar = infoFlashBar(message)
        flashbar?.show()
    }

    override fun onWarning(message: String) {
        flashbar = warningFlashBar(message)
        flashbar?.show()
    }

    override fun onDanger(message: String) {
        flashbar = dangerFlashBar(message)
        flashbar?.show()
    }




}