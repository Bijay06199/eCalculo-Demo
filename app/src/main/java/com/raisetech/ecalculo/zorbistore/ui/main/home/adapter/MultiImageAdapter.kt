package com.raisetech.ecalculo.zorbistore.ui.main.home.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisetech.ecalculo.R
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Image
import kotlinx.android.synthetic.main.row_multiple_image.view.*

class MultiImageAdapter (private val listener:OnItemClickListener,val itemList:List<Image>):RecyclerView.Adapter<MultiImageAdapter.ViewHolder>(){


    private var index = -1


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultiImageAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.row_multiple_image,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =itemList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var imageUrl=itemList[position].src

        Glide.with(holder.image)
            .load(imageUrl)
            .placeholder(R.drawable.place_holder_icon)
            .into(holder.image)


        holder.image.setOnClickListener {
            listener.onImageSelect(holder.adapterPosition, itemList[holder.adapterPosition])
        }
    }




    inner class ViewHolder(var containerView: View):RecyclerView.ViewHolder(containerView){


        val image=containerView.imageProduct

    }


    interface OnItemClickListener {
        fun onImageSelect(position: Int,itemList: Image)
    }


    }
