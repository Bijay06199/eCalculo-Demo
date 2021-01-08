package com.raisetech.ecalculo.zorbistore.ui.main.home.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.Data
import kotlinx.android.synthetic.main.home_categories_recyclerview.view.*


//class HomeCategoriesAdapter (private val homeCategoryModel: List<HomeCategoryModel>,private val listener:HomeItemAdapter.OnItemClickListener,private  val listenerMain:OnItemClickListener)
//    :RecyclerView.Adapter<HomeCategoriesAdapter.ViewHolder>(){
//
//

//
//    override fun onCreateViewHolder(parent: ViewGroup,
//                                    viewType: Int): ViewHolder {
//        val v:HomeCategoriesRecyclerviewBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context)
//            , R.layout.home_categories_recyclerview,parent,false)
//        return ViewHolder(v)
//    }
//
//    override fun getItemCount(): Int =homeCategoryModel.size
//
//    override fun onBindViewHolder(holder: ViewHolder,
//                                  position: Int) {
//        holder.mBinding.model=homeCategoryModel[position]
//        val parent = homeCategoryModel[position]
//
//        val childLayoutManager = LinearLayoutManager( holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
//        childLayoutManager.initialPrefetchItemCount = 4
//        holder.recyclerView.apply {
//            layoutManager = childLayoutManager
//            adapter = HomeItemAdapter(parent.homeItemModel,listener)
//
//            setRecycledViewPool(viewPool)
//        }
//
//        holder.mBinding.tvViewAll.setOnClickListener {
//            listenerMain.onChildClicked(holder.adapterPosition,homeCategoryModel[holder.adapterPosition])
//        }
//
//    }
//
//    interface OnItemClickListener{
//
//        fun onChildClicked(position: Int,childItems: HomeCategoryModel)
//    }
//
//    inner class ViewHolder(val mBinding :HomeCategoriesRecyclerviewBinding) : RecyclerView.ViewHolder(mBinding.root){
//        val recyclerView : RecyclerView = itemView.rv_item
//    }
//
//
//}

class HomeCategoriesAdapter(private val context: Context,private val listener:OnItemClickListener, var itemList:ArrayList<Data>): RecyclerView.Adapter<HomeCategoriesAdapter.FeaturedViewHolder>(){
var name:String?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_categories_recyclerview,parent,false)
        return FeaturedViewHolder(view)
    }

    override fun getItemCount(): Int=9

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {


        for (i in 0 until itemList.size){
             name=itemList[position].name


            if (!name.equals("Slider", ignoreCase = true) && !name.equals(
                            "Uncategorized", ignoreCase = true) && !name.equals("Slider Banners",ignoreCase = true)
            ) {
//            holder.setVisibility(true)
//            val images=itemList[position].image?.src
//            Glide.with(holder.categoryImage)
//                .load(images)
//                .placeholder(R.drawable.place_holder_icon)
//                .into(holder.categoryImage)

                holder.categoryName.text=itemList[position].name
//            holder.categoryName.setText(itemList[position].name)
            } else {
                holder.lLTitle.visibility=View.GONE
                holder.lLTitle.setLayoutParams(RecyclerView.LayoutParams(0,0))

            }



            holder.lLTitle.setOnClickListener {
                listener.onSelectListener(holder.adapterPosition,itemList[holder.adapterPosition])
            }

        }





    }



    interface OnItemClickListener{
        fun onSelectListener(position: Int,itemList: Data)
    }


    inner class FeaturedViewHolder(val containerView: View):
        RecyclerView.ViewHolder(containerView){
        val categoryName=containerView.tv_home_category_name
        val categoryImage=containerView.iv_home_categories_image
        val lLTitle=containerView.lLTitle


//        fun setVisibility(isVisible: Boolean) {
//            val param =
//                itemView.getLayoutParams() as RecyclerView.LayoutParams
//            if (isVisible) {
//                param.height = LinearLayout.LayoutParams.WRAP_CONTENT
//                param.width = LinearLayout.LayoutParams.MATCH_PARENT
//                itemView.setVisibility(View.VISIBLE)
//            } else {
//                itemView.setVisibility(View.GONE)
//                param.height = 0
//                param.width = 0
//            }
//            itemView.setLayoutParams(param)
//        }

    }



    fun addAll(itemList: ArrayList<Data>){
        this.itemList=itemList
        notifyDataSetChanged()

    }


}