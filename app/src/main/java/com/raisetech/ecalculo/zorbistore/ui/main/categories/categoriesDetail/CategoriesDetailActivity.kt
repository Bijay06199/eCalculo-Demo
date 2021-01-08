package com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail


import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.base.BaseActivity
import com.raisetech.ecalculo.zorbistore.constants.Constants
import com.raisetech.ecalculo.zorbistore.ui.main.MainActivity
import com.raisetech.ecalculo.zorbistore.ui.main.cart.ShoppingCart
import com.raisetech.ecalculo.zorbistore.ui.main.categories.adapter.SlidingAdapterTransaction
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.DataX
import com.raisetech.ecalculo.zorbistore.ui.main.shop.adapter.ProductAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class CategoriesDetailActivity :
    BaseActivity<com.raisetech.ecalculo.databinding.ActivityCategoriesDetailBinding, CategoriesDetailViewModel>(), ProductAdapter.OnItemClickListener {

    var tab: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_categories_detail
    override fun getViewModel(): CategoriesDetailViewModel = categoriesDetailViewModel
    private val categoriesDetailViewModel: CategoriesDetailViewModel by viewModel()
    var category: String? = null
    var groupName: String? = null
    var categoryId: Int? = null
    var id:ArrayList<Int>?=null
    var title:ArrayList<String>?=null
    lateinit var productAdapter: ProductAdapter
    var totalCategoriesList: Array<String>? = null
    var categoriesList = ArrayList<CategoriesResponse>()
    var itemList=ArrayList<Data>()
    var mainActivity: MainActivity?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        productAdapter= ProductAdapter(this, this@CategoriesDetailActivity, itemList)
        category = intent.getStringExtra(Constants.CategoryName)
        categoryId = intent.getIntExtra(Constants.CategoryId, 0)

        if (savedInstanceState == null) {
            CategoriesItemFragment.start(this@CategoriesDetailActivity,R.id.categories_container,categoryId)

        }
        initView()
        tabListener()


    }

    private fun tabListener() {
        with(viewDataBinding) {

            with(categoriesDetailViewModel){

                var itemList=ArrayList<DataX>()
                subCategories()

                subCategoryEvent.observe(this@CategoriesDetailActivity, Observer {



                        subCategory!!.data.forEach { i ->
                            groupName = i.groupName
                            categoryId = i.id
                            var categoryName = i.name

                            itemList.add(DataX(groupName!!, categoryId!!, categoryName!!))

                            tablayoutHeadlines.addTab(tablayoutHeadlines.newTab().setText(categoryName))


                        }


                    for (i in 0 until itemList.size){

                        tablayoutHeadlines.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabReselected(tab: TabLayout.Tab?) {


                            }

                            override fun onTabUnselected(tab: TabLayout.Tab?) {

                            }

                            override fun onTabSelected(tab: TabLayout.Tab?) {
                                when(tab?.position){

                                    i->{
                                        CategoriesItemFragment.start(this@CategoriesDetailActivity,R.id.categories_container,categoryId)
                                        tablayoutHeadlines.getTabAt(i)?.text=category

                                    }
                                    i+1->{
                                        CategoriesSubItemFragment.start(this@CategoriesDetailActivity,R.id.categories_container,itemList[i].id)
                                        tablayoutHeadlines.getTabAt(i+1)?.text=itemList[i].name

                                    }
                                }

                            }


                        })

                    }


                    tablayoutHeadlines.getTabAt(0)?.text=category



                })

            }
        }
    }







    private fun initView() {
        with(viewDataBinding) {


            tvCategoryName.setText(category)
            topBar.setOnClickListener {
                finish()
            }
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onItemSelect(position: Int, itemList: Data) {

    }

    override fun onLayoutAddClick(position: Int, itemList: Data) {

        mainActivity!!.badge.number= ShoppingCart.getCart().size
    }

    override fun onAddClick(position: Int, itemList: Data) {
        mainActivity!!.badge.number=ShoppingCart.getCart().size

    }

    override fun onSubtractClick(position: Int, itemList: Data) {
        mainActivity!!.badge.number=ShoppingCart.getCart().size

    }
}
