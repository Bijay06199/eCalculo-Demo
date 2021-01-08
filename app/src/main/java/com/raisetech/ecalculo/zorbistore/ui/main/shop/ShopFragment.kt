package com.raisetech.ecalculo.zorbistore.ui.main.shop


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseFragment
import com.raisetech.ecalculo.zorbistore.ui.main.ImageActivity
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.home.adapter.HomeCategoriesAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.home.adapter.MultiImageAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.adapter.ProductAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Image
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import kotlinx.android.synthetic.main.persistent_bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShopFragment : BaseFragment<com.raisetech.ecalculo.databinding.FragmentShopBinding, ShopViewModel>(),
    ProductAdapter.OnItemClickListener,
     MultiImageAdapter.OnItemClickListener
{

    override fun getLayoutId(): Int = R.layout.fragment_shop
    override fun getViewModel(): ShopViewModel = shopViewModel
    private val shopViewModel: ShopViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    lateinit var productAdapter: ProductAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var multiImageAdapter:MultiImageAdapter
    var itemList = ArrayList<Data>()



    var isLoading: Boolean = false
    var page = 1
    var limit = 20



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager=GridLayoutManager(requireContext(),2)
        viewDataBinding.recyclerViewAll.layoutManager=layoutManager
        getData()
        initView()





        viewDataBinding.recyclerViewAll.addOnScrollListener(object :RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount=layoutManager.childCount
                val pastVisibleItem=layoutManager.findFirstCompletelyVisibleItemPosition()
                 val total=productAdapter.itemCount

                if (!isLoading){

                    if ((visibleItemCount + pastVisibleItem)>=total){
                        page++
                        getData()

                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })


        bottomSheetBehavior = BottomSheetBehavior.from<MaterialCardView>(persistent_bottom_sheet)

        if (savedInstanceState == null) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {


                    BottomSheetBehavior.STATE_EXPANDED -> {
                        viewDataBinding.cvWithBottomSheet.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        viewDataBinding.cvWithBottomSheet.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED.and(20) -> {
                        viewDataBinding.cvWithBottomSheet.visibility = View.VISIBLE
                    }

                }
            }

        })
    }




    private fun initView() {

        with(viewDataBinding) {


            //       swipeRefreshLayout = requireView().findViewById(R.id.swipe)
            var animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotation_anim)
            animation.setInterpolator(LinearInterpolator())
            progressBar4.startAnimation(animation)

            eTSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    filter(p0.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }


            })




            setupUI(hideKeyboard)



            ivBack.setOnClickListener {
                fragmentManager?.popBackStack()
            }
        }
    }

    fun filter(text: String) {

        val filterdNames: ArrayList<Data> = ArrayList()

        for (s in itemList) {
            //if the existing elements contains the search input
            if (s.name?.toLowerCase()?.contains(text.toLowerCase())!!) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }

        productAdapter.filteredList(filterdNames)
    }


    private fun getData() {
        with(viewDataBinding) {

            isLoading=true
            with(shopViewModel) {
                getProduct(page, limit)

                productEvent.observe(viewLifecycleOwner, Observer {




                    productAdapter= ProductAdapter(requireContext(),this@ShopFragment,itemList)
                    recyclerViewAll.adapter=productAdapter


                    productName?.data?.forEach { i->
                        var id=i.id
                        var description=i.description
                      var name=i.name
                      var price=i.price
                      var on_sale=i.on_sale
                      var in_stock=i.in_stock
                      var images=i.images
                      var categories=i.categories




                      itemList.add(Data(id,description,categories,false,images,in_stock,name,on_sale,price,false,false,name))
                  }
                    productAdapter.addAll(itemList)
                    progressBar4.visibility=View.GONE

                })


                    Handler().postDelayed({
                        if (::productAdapter.isInitialized) {
                            productAdapter.notifyDataSetChanged()
                        }
                        else{
                            productAdapter= ProductAdapter(requireContext(),this@ShopFragment,itemList)
                            recyclerViewAll.adapter=productAdapter
                        }
                        isLoading=false
                        progressBar4.visibility=View.GONE
                    },5000)




            }

        }

    }


    companion object {
        val TAG = ShopFragment::class.java.simpleName
        fun start(activity: FragmentActivity, containerId: Int) {
            val fragment = ShopFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(TAG)
                .commit()
        }

    }



    override fun onItemSelect(position: Int, itemList: Data) {

        with(viewDataBinding){

            for (i in 0..position) {

                var cartItemModel = CartItemModel(itemList, 0)

                var _counter = cartItemModel.quantity



                btnAddToCart.setOnClickListener {

                    _counter++

                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, requireContext())
                    btnAddToCart.visibility = View.GONE
                    addSubtractButton.visibility = View.VISIBLE
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tvCartQuantity.setText(Integer.toString(_counter))
                }


                clAdd.setOnClickListener { view ->

                    _counter++
                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, requireContext())

                    ShoppingCart.getCart()
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tvCartQuantity.setText(Integer.toString(_counter))


                }


                clSubtract.setOnClickListener {

                    _counter--
                    val item = CartItemModel(itemList)
                    ShoppingCart.removeItem(item, requireContext())
                    ShoppingCart.getCart()
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tvCartQuantity.setText(Integer.toString(_counter))

                    if (_counter == 0) {
                        ShoppingCart.removeItem(item, requireContext())
                        btnAddToCart.visibility = View.VISIBLE
                        (context as MainActivity).badge.number=ShoppingCart.getCart().size
                        addSubtractButton.visibility = View.GONE

                    }

                }
            }

            btnAddToCart.visibility = View.VISIBLE
            addSubtractButton.visibility = View.GONE


        }


        with(viewDataBinding) {


            var images = itemList.images
            multiImageAdapter= MultiImageAdapter(this@ShopFragment,
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

            var price = itemList.price

            if (description!=null){

                viewDataBinding.tvProductDescriptions.setText("Product Descriptions")
                viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))

            }
            else{
                viewDataBinding.tvProductDescriptions.setText("")
                viewDataBinding.tvDescriptions.setText("")

            }

            viewDataBinding.tvProductName1.setText(productName)
            viewDataBinding.tvProductName.setText(productName)
            viewDataBinding.tvPrice.setText(price.toString())
            viewDataBinding.tvTotalAmount.setText(price.toString())


            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED.and(20)
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
            this@ShopFragment.activity,
            ImageActivity::class.java
        )
        intent.putExtra("images",itemList.src)
        startActivity(intent)
    }


}