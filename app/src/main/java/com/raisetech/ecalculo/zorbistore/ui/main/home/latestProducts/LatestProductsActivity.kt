package com.raisetech.ecalculo.zorbistore.ui.main.home.latestProducts


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.raisetech.ecalculo.BR
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.ui.main.ImageActivity
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.home.adapter.MultiImageAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.home.latestProducts.adapter.LatestAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Image
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import kotlinx.android.synthetic.main.persistent_bottom_sheet.persistent_bottom_sheet
import org.koin.androidx.viewmodel.ext.android.viewModel

class LatestProductsActivity : BaseActivity<com.raisetech.ecalculo.databinding.ActivityLatestProductsBinding, LatestProductViewModel>(),
    LatestAdapter.OnItemClickListener ,
    MultiImageAdapter.OnItemClickListener
{

    override fun getLayoutId(): Int =R.layout.activity_latest_products
    override fun getViewModel(): LatestProductViewModel =latestProductViewModel
    private val latestProductViewModel:LatestProductViewModel by viewModel()
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }



    lateinit var multiImageAdapter:MultiImageAdapter
    lateinit var latestAdapter: LatestAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setUpRecyclerView()
        bottomSheetBehavior= BottomSheetBehavior.from<MaterialCardView>(persistent_bottom_sheet)

        if (savedInstanceState==null){
            bottomSheetBehavior.state= BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){


                    BottomSheetBehavior.STATE_EXPANDED->{
                        viewDataBinding.cvWithBottomSheet.visibility= View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN->{
                        viewDataBinding.cvWithBottomSheet.visibility= View.GONE
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED.and(20)->{
                        viewDataBinding.cvWithBottomSheet.visibility= View.VISIBLE
                    }

                }
            }

        })

    }

    private fun setUpRecyclerView() {
        with(viewDataBinding){
            with(latestProductViewModel){

                var productList = ArrayList<Data>()
                var cartItems=ArrayList<CartItemModel>()

                latestAdapter = LatestAdapter(this@LatestProductsActivity,productList, ShoppingCart.getCart())


                getLatestProduct()

                latestProductEvent.observe(this@LatestProductsActivity, Observer {
                    recyclerViewAll.adapter = latestAdapter
                    latestProduct?.data?.forEach { i->
                        var id=i.id
                        var description=i.description
                        var categories=i.categories
                        var downloadable=i.downloadable
                        var images=i.images
                        var in_stock=i.in_stock
                        var name=i.name
                        var on_sale=i.on_sale
                        var price=i.price
                        var shipping_required=i.shipping_required
                        var shipping_taxable=i.shipping_taxable
                        var title=i.title

                        productList.add(com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data(id,description,categories,downloadable,images,in_stock

                                ,name,on_sale,price,shipping_required,shipping_taxable,title
                        ))


                    }
                    latestAdapter.addAll(productList)
                    progressBar4.visibility=View.GONE

                })
            }
        }
    }

    private fun initView() {
        with(viewDataBinding){
            var animation= AnimationUtils.loadAnimation(this@LatestProductsActivity, R.anim.rotation_anim)
            animation.setInterpolator ( LinearInterpolator() )
            progressBar4.startAnimation(animation)
            ivBack.setOnClickListener {
                startActivity(Intent(this@LatestProductsActivity, MainActivity::class.java))

            }
        }
    }

    override fun onItemLatestSelect(position: Int, itemList: Data) {


        with(viewDataBinding){

            for (i in 0 ..position){

                var cartItemModel = CartItemModel(itemList, 0)

                var _counter = cartItemModel.quantity



                btnAddToCart.setOnClickListener {

                    _counter++

                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, this@LatestProductsActivity)
                    btnAddToCart.visibility = View.GONE
                    addSubtractButton.visibility = View.VISIBLE
                    tvCartQuantity.setText(Integer.toString(_counter))
                    notifyChange()
                }


                clAdd.setOnClickListener { view ->

                    _counter++
                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, this@LatestProductsActivity)

                    ShoppingCart.getCart()
                    tvCartQuantity.setText(Integer.toString(_counter))
                    notifyChange()


                }


                clSubtract.setOnClickListener {

                    _counter--
                    val item = CartItemModel(itemList)
                    ShoppingCart.removeItem(item, this@LatestProductsActivity)
                    ShoppingCart.getCart()
                    tvCartQuantity.setText(Integer.toString(_counter))

                    if (_counter == 0) {
                        ShoppingCart.removeItem(item, this@LatestProductsActivity)
                        btnAddToCart.visibility = View.VISIBLE
                        addSubtractButton.visibility = View.GONE

                    }
                    notifyChange()
                }



                var images = itemList.images
                multiImageAdapter= MultiImageAdapter(this@LatestProductsActivity,
                    images as List<Image>
                )
                viewDataBinding.rvMultiImage.adapter=multiImageAdapter




                if (images?.size!! >1){

                    viewDataBinding.viewPagerIndicatorIconsLayout.visibility=View.VISIBLE

                }
                else{
                    viewDataBinding.viewPagerIndicatorIconsLayout.visibility=View.GONE
                }


                var productName = itemList.name
                var description = itemList.description

                if (description!=null){

                    viewDataBinding.textView20.setText("Product Descriptions")
                    viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))

                }
                else{
                    viewDataBinding.textView20.setText("")
                    viewDataBinding.tvDescriptions.setText("")
                }
                var price = itemList.price

                viewDataBinding.tvProductName1.setText(productName)
                viewDataBinding.tvProductName.setText(productName)
               viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))
                viewDataBinding.tvPrice.setText(price.toString())
                viewDataBinding.tvTotalAmount.setText(price.toString())

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED.and(20)
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            btnAddToCart.visibility = View.VISIBLE
            addSubtractButton.visibility = View.GONE


        }


    }

    override fun onLayoutAddLatestClick(position: Int, itemList: Data) {

    }

    override fun onImageSelect(position: Int, itemList: Image) {
        var intent= Intent(
            this,
            ImageActivity::class.java
        )
        intent.putExtra("images",itemList.src)
        startActivity(intent)
    }
}