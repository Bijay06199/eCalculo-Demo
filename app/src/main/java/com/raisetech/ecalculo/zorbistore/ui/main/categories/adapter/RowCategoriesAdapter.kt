package com.raisetech.ecalculo.zorbistore.ui.main.categories.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.Data
import kotlinx.android.synthetic.main.row_categories.view.*

class RowCategoriesAdapter( private val listener:OnItemClickListener, var itemList:ArrayList<Data>): RecyclerView.Adapter<RowCategoriesAdapter.FeaturedViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {

        val view= LayoutInflater.from(parent.context).inflate(R.layout.row_categories,parent,false)
        return FeaturedViewHolder(view)
    }

    override fun getItemCount(): Int=itemList.size

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {

        var name=itemList[position].name
        if (!name.equals("Slider", ignoreCase = true) && !name.equals(
                "Uncategorized", ignoreCase = true) && !name.equals("Slider Banners",ignoreCase = true)
        ){
            holder.categoryName.text=itemList[position].name
        } else {
            holder.clClick.setVisibility(View.GONE)
            holder.clClick.layoutParams.height=0
            holder.clClick.layoutParams.width=0
        }
        holder.categoryName.text=itemList[position].name

        holder.clClick.setOnClickListener {
            listener.onCategorySelectListener(holder.adapterPosition,itemList[holder.adapterPosition])
        }
    }

    interface OnItemClickListener{
        fun onCategorySelectListener(position: Int,itemList: Data)
    }


    inner class FeaturedViewHolder(val containerView: View):
        RecyclerView.ViewHolder(containerView){
        val categoryName=containerView.tVCategories
        val clClick=containerView.lLTitle
    }



    fun addAll(itemList: ArrayList<Data>){
        this.itemList=itemList
        notifyDataSetChanged()
    }
}