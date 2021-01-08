package com.raisetech.ecalculo.zorbistore.ui.main.categories.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.raisetech.ecalculo.zorbistore.ui.main.categories.categoriesDetail.CategoriesItemFragment
import java.lang.RuntimeException

class SlidingAdapterTransaction ( var fm:FragmentManager,internal var totalTabs: Int,var id:ArrayList<Int>, var title:ArrayList<String>) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }


    override fun getItem(position: Int): Fragment {
        return CategoriesItemFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }


}
