package com.raisetech.ecalculo.zorbistore.ui.main.home


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.raisetech.ecalculo.BR
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseFragment
import com.raisetech.ecalculo.zorbistore.constants.Constants
import com.raisetech.ecalculo.zorbistore.ui.main.ImageActivity
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.cart.model.CartItemModel
import com.raisetech.ecalculo.zorbistore.ui.main.categories.CategoriesFragment
import com.raisetech.ecalculo.zorbistore.ui.main.categories.adapter.RowCategoriesAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail.CategoriesDetailActivity
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.Data
import com.raisetech.ecalculo.zorbistore.ui.main.contact.ContactFragment
import com.raisetech.ecalculo.zorbistore.ui.main.home.adapter.*
import com.raisetech.ecalculo.zorbistore.ui.main.home.featuredProduct.FeaturedProductActivity
import com.raisetech.ecalculo.zorbistore.ui.main.home.latestProducts.LatestProductsActivity
import com.raisetech.ecalculo.zorbistore.ui.main.home.onSale.OnSaleActivity
import com.raisetech.ecalculo.zorbistore.ui.main.shop.adapter.ProductAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Image
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import com.raisetech.ecalculo.zorbistore.utils.AuthListenerInfo
import com.raisetech.ecalculo.zorbistore.utils.extentions.dangerFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.infoFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.successFlashBar
import com.raisetech.ecalculo.zorbistore.utils.extentions.warningFlashBar
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.product_items_recyclerview.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment<com.raisetech.ecalculo.databinding.FragmentHomeBinding, HomeViewModel>(),
    HomeCategoriesAdapter.OnItemClickListener,
    ProductAdapter.OnItemClickListener,
    LatestProductAdapter.OnItemClickListener,
    OnSaleProductAdapter.OnItemClickListener,
    MultiImageAdapter.OnItemClickListener,
    FeaturedProductAdapter.OnItemClickListener,
    RowCategoriesAdapter.OnItemClickListener, AuthListenerInfo {

    override fun getLayoutId(): Int = R.layout.fragment_home
    override fun getViewModel(): HomeViewModel = homeViewModel
    private val homeViewModel: HomeViewModel by viewModel()


    var flashbar: Flashbar? = null
    var name: String? = null
    var cartQuantity:Int=1
    var itemList = ArrayList<NewProductResponseItem>()
    lateinit var categoriesAdapter: RowCategoriesAdapter
    lateinit var latestProductAdapter: LatestProductAdapter
    lateinit var featuredProductAdapter: FeaturedProductAdapter
    lateinit var homeCategoriesAdapter: HomeCategoriesAdapter
    lateinit var onSaleProductAdapter: OnSaleProductAdapter
 //   lateinit var bannerAdapter: BannerAdapter
    lateinit var multiImageAdapter: MultiImageAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    var cartItemModel: CartItemModel?=null




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Paper.init(requireContext())
        initView()
        setupUI(viewDataBinding.drawer)
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
    //    setUpBanner()
        setUpLatestProduct()
        setUpOnSaleProduct()
        setUpFeaturedProduct()
        setUpCategory()
        setUpNavigationItems()
        setTimer()

    }



//    fun setUpBanner() {
//        with(viewDataBinding) {
//            with(homeViewModel) {
//                var rv = viewpager.getChildAt(0) as RecyclerView
//                rv.clipToPadding = false
//
//                var categoryList = ArrayList<NewProductResponseItem>()
//
//                getBanner()
//                bannerAdapter = BannerAdapter(categoryList)
//
//
//                bannerEvent.observe(viewLifecycleOwner, Observer {
//
//
//                    categoryList.addAll(banner!!)
//                    viewDataBinding.viewpager.adapter = bannerAdapter
//                    bannerAdapter.notifyDataSetChanged()
//
//                })
//
//            }
//        }
//    }

    fun setUpLatestProduct() {
        with(viewDataBinding) {
        with(homeViewModel) {



            clLatestProduct.setOnClickListener {
                startActivity(Intent(this@HomeFragment.activity, LatestProductsActivity::class.java))
            }
            var productList = ArrayList<com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data>()
            var cartItem=ArrayList<CartItemModel>()

            latestProductAdapter = LatestProductAdapter(requireContext(),this@HomeFragment, productList)


            getLatestProduct()

            latestProductEvent.observe(viewLifecycleOwner, Observer {
                rvRecentProducts.adapter = latestProductAdapter
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
                latestProductAdapter.addAll(productList)
                progressBar3.visibility=View.GONE

                clOnsale.visibility=View.VISIBLE
                clCategories.visibility=View.VISIBLE
                clFeaturedProduct.visibility=View.VISIBLE


            })

        }
    }
}

    fun setUpOnSaleProduct() {

        with(viewDataBinding) {
            with(homeViewModel) {

                clOnsale.setOnClickListener {
                    startActivity(Intent(this@HomeFragment.activity, OnSaleActivity::class.java))
                }
                var productList = ArrayList<com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data>()
                var cartItem=ArrayList<CartItemModel>()


                onSaleProductAdapter = OnSaleProductAdapter(this@HomeFragment, productList)
                getOnSaleProduct()
                onSaleProductEvent.observe(viewLifecycleOwner, Observer {
                    rvOnsaleProducts.adapter = onSaleProductAdapter
                onSale?.data?.forEach { i->
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

                    onSaleProductAdapter.addAll(productList)


                })

            }
        }
    }

    fun setUpFeaturedProduct() {
        with(viewDataBinding) {
            clFeaturedProduct.setOnClickListener {
                startActivity(Intent(this@HomeFragment.activity, FeaturedProductActivity::class.java))
            }
            with(homeViewModel) {
                var productList = ArrayList<com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data>()


                featuredProductAdapter = FeaturedProductAdapter(this@HomeFragment, productList)
                getFeaturedProduct()
                featuredProductEvent.observe(viewLifecycleOwner, Observer {

                    rvFeaturedProducts.adapter = featuredProductAdapter
                    featuredProductName?.data?.forEach { i->
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

                        featuredProductAdapter.addAll(productList)

                    }
                })
            }
        }
    }
    fun setTimer() {

        var numPages:Int=3
        var curPage:Int=0
        val handler = Handler()
        val Update = object:Runnable {
            internal var NUM_PAGES = numPages
            internal var currentPage = curPage
            public override fun run() {
                if (currentPage == NUM_PAGES)
                {
                    currentPage = 0
                }
                viewDataBinding.viewpager.setCurrentItem(currentPage++, true)
            }
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object: TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 100, 3000)
    }



    private fun setUpCategory() {
        with(viewDataBinding) {
            with(homeViewModel) {

                var categoryList = ArrayList<Data>()
                homeCategoriesAdapter = HomeCategoriesAdapter(requireContext(), this@HomeFragment, categoryList)
                categories()
                rvCategories.adapter = homeCategoriesAdapter
                categoriesEvent.observe(viewLifecycleOwner, Observer {

                    categoryName?.data?.forEach{i->
                        val id =i.id
                        val name=i.name
                        val slug=i.slug

                        categoryList.add(Data(id,name,slug))
                    }
                    
                   homeCategoriesAdapter.addAll(categoryList)

                })


            }


        }
    }

    private fun setUpNavigationItems() {
        with(homeViewModel) {
            homeViewModel.authListenerInfo = this@HomeFragment
            with(viewDataBinding) {
                val sideCategoryItems = sideNavigation.findViewById<RecyclerView>(R.id.rvMenuList)

                var itemList = ArrayList<Data>()
                categoriesAdapter =
                    RowCategoriesAdapter(this@HomeFragment, itemList)
                categoriesNavigation()

                categoriesEventNavigation.observe(viewLifecycleOwner, Observer {

                    categoryNavigation?.data?.forEach{i->
                        val id=i.id
                        val name=i.name
                        val slug=i.slug

                        itemList.add(Data(id,name,slug))
                    }

                    sideCategoryItems.adapter = categoriesAdapter
                    categoriesAdapter.addAll(itemList)




                })


            }
        }
    }




    private fun initView() {
        with(viewDataBinding) {

            var animation= AnimationUtils.loadAnimation(requireContext(),R.anim.rotation_anim)
            animation.setInterpolator ( LinearInterpolator() )
            progressBar3.startAnimation(animation)

            clOnsale.visibility=View.GONE
            clFeaturedProduct.visibility=View.GONE
            clCategories.visibility=View.GONE

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                 //   filter(p0.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }


            })

            tvUserName.setText(preferenceManager.getFirstName())
            clCategories.setOnClickListener {
                CategoriesFragment.start(requireActivity(),R.id.main_screen_container)
            }

            progressBar3.visibility=View.VISIBLE
            lLContactUs.setOnClickListener {
                ContactFragment.start(requireActivity(), R.id.main_screen_container)
            }

            collapseIcon.setOnClickListener {
                drawer.openDrawer(GravityCompat.START)
            }




        }
    }


//    fun filter(text:String){
//
//        val filterdNames: ArrayList<NewProductResponseItem> = ArrayList()
//
//
//            for (s in itemList) {
//                //if the existing elements contains the search input
//                if (s.name?.toLowerCase()?.contains(text.toLowerCase())!!) {
//                    //adding the element to filtered list
//                    filterdNames.add(s)
//                }
//            }
//
//        featuredProductAdapter.filteredList(filterdNames)
//
//
//    }

    companion object {

        val TAG = HomeFragment::class.java.simpleName
        fun start(activity: FragmentActivity, containerId: Int) {
            val fragment = HomeFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(TAG)
                .commit()
        }



    }


    override fun onSelectListener(position: Int, itemList: Data) {

        for (i in 0..position) {
            var categoryName = itemList.name
            var categoryId=itemList.id
            val intent = Intent(this.activity, CategoriesDetailActivity::class.java)
            intent.putExtra(Constants.CategoryName, categoryName)
            intent.putExtra(Constants.CategoryId,categoryId)
            startActivity(intent)
            viewDataBinding.drawer.closeDrawers()

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



    override fun onItemSelect(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {

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
            multiImageAdapter= MultiImageAdapter(this@HomeFragment,
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

                viewDataBinding.textView20.setText("Product Descriptions")
                viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))

            }
            else{
                viewDataBinding.textView20.setText("")
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

    override fun onLayoutAddClick(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {

    }



    override fun onAddClick(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {

    }

    override fun onSubtractClick(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {

    }


    override fun onItemLatestSelect(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {


        with(viewDataBinding){

            for (i in 0 ..position){

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
                    notifyChange()
                }


                clAdd.setOnClickListener { view ->

                    _counter++
                    val item = CartItemModel(itemList)
                    ShoppingCart.addItem(item, requireContext())

                    ShoppingCart.getCart()
                    (context as MainActivity).badge.number=ShoppingCart.getCart().size
                    tvCartQuantity.setText(Integer.toString(_counter))
                    notifyChange()


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
                    notifyChange()
                }



                var images = itemList.images
                multiImageAdapter= MultiImageAdapter(this@HomeFragment,
                    images as List<Image>
                )
                viewDataBinding.rvMultiImage.adapter=multiImageAdapter




                if (images?.size!! >1){

                    viewDataBinding.viewPagerIndicatorIconsLayout.visibility=View.VISIBLE

                }
                else{
                    viewDataBinding.viewPagerIndicatorIconsLayout.visibility=View.GONE
                }


                for (i in 0 until images!!.size) {

                    var productImage = images[i]?.src
                 //   setImageSrc(viewDataBinding.ivProduct, productImage!!)

//                    viewDataBinding.ivProduct.setOnClickListener {
//                        var intent= Intent(
//                            this@HomeFragment.activity,
//                            ImageActivity::class.java
//                        )
//                        intent.putExtra("images",images[i]?.src)
//                        startActivity(intent)
//
//                    }

                }

                var productName = itemList.name
                var description = itemList.description

                if (description!=null){

                    viewDataBinding.textView20.setText("Product Descriptions")
                    viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))

                }
                else{
                    viewDataBinding.textView20.setText("")
                }

                var price = itemList.price

                viewDataBinding.tvProductName1.setText(productName)
                viewDataBinding.tvProductName.setText(productName)
         //       viewDataBinding.tvDescriptions.setText(Html.fromHtml(description))
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

    override fun onLayoutAddLatestClick(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {

    }


    override fun onItemSaleSelect(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {


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


        var images = itemList.images
        multiImageAdapter= MultiImageAdapter(this@HomeFragment,
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

        var regularPrice=itemList.price
        var saleprice=itemList.price

//        if (saleprice==""){
//            viewDataBinding.tvRegularPrice.setText("")
//        }
//        else{
//            viewDataBinding.tvRegularPrice.setText(regularPrice)
//        }

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


        viewDataBinding.btnAddToCart.visibility = View.VISIBLE
        viewDataBinding.addSubtractButton.visibility = View.GONE


    }

    override fun onSaleLayoutAddClick(position: Int, itemList: com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data) {

    }

    override fun onImageSelect(position: Int, itemList: Image) {

//            setImageSrc(viewDataBinding.ivProduct,itemList.src!!)


                var intent= Intent(
                    this@HomeFragment.activity,
                    ImageActivity::class.java
                )
                intent.putExtra("images",itemList.src)
                startActivity(intent)






    }



    override fun onCategorySelectListener(position: Int, itemList: Data) {

        for (i in 0..position) {
            var categoryName = itemList.name
            var categoryId=itemList.id
            val intent = Intent(this.activity, CategoriesDetailActivity::class.java)
            intent.putExtra(Constants.CategoryName, categoryName)
            intent.putExtra(Constants.CategoryId,categoryId)
            startActivity(intent)
            viewDataBinding.drawer.closeDrawers()

        }
    }


}