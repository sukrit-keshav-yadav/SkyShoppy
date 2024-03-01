package com.hyskytech.skyshoppy.Adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hyskytech.skyshoppy.databinding.ColorRvItemBinding
import com.hyskytech.skyshoppy.databinding.SizesRvItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

    private var selectedPosition = -1
    inner class SizesViewHolder(private val binding : SizesRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(size : String,position: Int){
            binding.imageSizesTV.text=size
            //if size is selected
            if (position==selectedPosition){
                binding.apply {
                    imageSizes.visibility= View.VISIBLE
                    imageShadowSize.visibility = View.VISIBLE
                }
            }else{//if size is not selected
                binding.apply {
                    imageSizes.visibility= View.INVISIBLE
//                    imageShadowColors.visibility = View.INVISIBLE
                }
            }
        }

    }

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            SizesRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size,position)

        holder.itemView.setOnClickListener{
            if (selectedPosition >=0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }

    var onItemClick:((String)->Unit)?=null
}