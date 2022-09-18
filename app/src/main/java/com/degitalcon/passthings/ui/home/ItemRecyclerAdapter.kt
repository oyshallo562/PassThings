package com.degitalcon.passthings.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.degitalcon.passthings.R

class ItemRecyclerAdapter(var context: Context, var List:ArrayList<Item>) :RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>() {

    var onItemClickListener:OnItemClickListener?=null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView!!){
        var imageIv: ImageView = itemView.findViewById(R.id.iv_home)
        var titleTv: TextView = itemView.findViewById(R.id.tv_title)
        var priceTv: TextView = itemView.findViewById(R.id.tv_Price)
        var passTv: TextView = itemView.findViewById(R.id.tv_pass)

        fun bind(item:Item){
            Glide.with(context).load(item.imageUrl).into(imageIv)
            titleTv.text = item.title
            priceTv.text = item.price
            passTv.text = item.pass

            //tv_title.?text = data.title
            imageIv.setOnClickListener {
                if(onItemClickListener!=null)
                    onItemClickListener?.onItemClick(item)
            }

        }
    }

    interface OnItemClickListener{
        fun onItemClick(photo:Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_add_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return List.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item=List[position]
        holder.bind(item)
    }

}