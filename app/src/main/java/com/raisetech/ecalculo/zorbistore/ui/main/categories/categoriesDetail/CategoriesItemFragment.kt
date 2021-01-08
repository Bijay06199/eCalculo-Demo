package com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail


import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import io.paperdb.Paper
import kotlinx.android.synthetic.main.persistent_bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.graphics.PaintFlagsDrawFilter
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseFragment
import com.raisetech.ecalculo.zorbistore.constants.Constants
import com.raisetech.ecalculo.zorbistore.ui.main.ImageActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.home.adapter.MultiImageAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.adapter.ProductAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Image
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem


class CategoriesItemFragment : BaseFragment<com.raisetech.ecalculo.databinding.FragmentCategoriesItemBinding, CategoriesItemViewModel>(),
    ProductAdapter.OnItemClickListener,
        MultiImageAdapter.OnItemClickListener

{




    override fun getLayoutId(): Int = R.layout.fragment_categories_item
    override fun getViewModel(): CategoriesItemViewModel =categoriesItemViewModel
    private val categoriesItemViewModel:CategoriesItemViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    lateinit var multiImageAdapter: MultiImageAdapter
    lateinit var productAdapter: ProductAdapter
    var categoryId:Int?=null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Paper.init(requireContext())
        initView()
         categoryId=arguments?.getInt(Constants.CategoryId)
          setUpRecyclerView()

        bottomSheetBehavior=BottomSheetBehavior.from<MaterialCardView>(persistent_bottom_sheet)

        if (savedInstanceState==null){
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.setBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
              when(newState){


                  BottomSheetBehavior.STATE_EXPANDED->{
                      viewDataBinding.cvWithBottomSheet.visibility=View.VISIBLE
                  }
                  BottomSheetBehavior.STATE_HIDDEN->{
                      viewDataBinding.cvWithBottomSheet.visibility=View.GONE
                  }
                  BottomSheetBehavior.STATE_HALF_EXPANDED.and(20)->{
                      viewDataBinding.cvWithBottomSheet.visibility=View.VISIBLE
                  }

              }
            }

        })
    }

    private fun initView() {
        with(viewDataBinding){



            var animation= AnimationUtils.loadAnimation(requireContext(),R.anim.rotation_anim)
            animation.setInterpolator ( LinearInterpolator() )
            progressBar5.startAnimation(animation)

            progressBar5.visibility=View.VISIBLE


        }
    }

    private fun setUpRecyclerView() {
        with(viewDataBinding){
            with(categoriesItemViewModel){

                var itemList=ArrayList<Data>()
                productAdapter= ProductAdapter(requireContext(),this@CategoriesItemFragment,itemList)


                getCategoryWise(categoryId)
                categoryWiseEvent.observe(viewLifecycleOwner, Observer {

                    rvItems.adapter=productAdapter
                    categoryWise?.data?.forEach { i ->
                        var id=i.id
                        var description=i.description
                        var name = i.name
                        var price = i.price
                        var on_sale = i.on_sale
                        var in_stock = i.in_stock
                        var images = i.images
                        var categories = i.categories

                        itemList.add(Data(id,description,categories,false,images,in_stock,name,on_sale,price,false,false,name))

                    }

                    productAdapter.addAll(itemList)
                    progressBar5.visibility=View.GONE

                })
            }

        }
    }

    companion object {


        fun start(activity: FragmentActivity, containerId:Int,id:Int?) {
            var fragment = CategoriesItemFragment()
             var bundle = Bundle()
            bundle.putInt(Constants.CategoryId, id!!)
            fragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction()
                    .replace(containerId,fragment)
                    .commit()
        }


    }



    override fun onItemSelect(position: Int, itemList: Data) {



        with(viewDataBinding){

            for (i in 0..position){

                var cartItemModel = CartItemModel(itemList, 0)

                var _counter = cartItemModel.quantity



                btnAddToCart.setOnClickListener {

                    _counter++

                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, requireContext())
                    btnAddToCart.visibility = View.GONE
                    addSubtractButton.visibility = View.VISIBLE
                    tvCartQuantity.setText(Integer.toString(_counter))
                }


                clAdd.setOnClickListener { view ->

                    _counter++
                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, requireContext())

                    ShoppingCart.getCart()
                    tvCartQuantity.setText(Integer.toString(_counter))


                }


                clSubtract.setOnClickListener {

                    _counter--
                    val item = CartItemModel(itemList)
                    ShoppingCart.removeItem(item, requireContext())
                    ShoppingCart.getCart()
                    tvCartQuantity.setText(Integer.toString(_counter))

                    if (_counter == 0) {
                        ShoppingCart.removeItem(item, requireContext())
                        btnAddToCart.visibility = View.VISIBLE
                        addSubtractButton.visibility = View.GONE

                    }
                }
                btnAddToCart.visibility = View.VISIBLE
                addSubtractButton.visibility = View.GONE




                var images = itemList.images

                multiImageAdapter= MultiImageAdapter(this@CategoriesItemFragment,
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
                viewDataBinding.tvPrice.setText(price.toString())
                viewDataBinding.tvCartAmount.setText(price.toString())

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED.and(20)
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

            }
        }

    }

    override fun onLayoutAddClick(position: Int, itemList: Data) {
    }

    override fun onAddClick(position: Int, itemList: Data) {
    }

    override fun onSubtractClick(position: Int, itemList: Data) {
    }

    override fun onImageSelect(position: Int, itemList: Image) {



                var intent= Intent(
                    this@CategoriesItemFragment.activity,
                    ImageActivity::class.java
                )
                intent.putExtra("images",itemList.src)
                startActivity(intent)





    }


}