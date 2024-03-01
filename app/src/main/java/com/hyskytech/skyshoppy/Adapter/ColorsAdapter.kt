package com.hyskytech.skyshoppy.Adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hyskytech.skyshoppy.databinding.ColorRvItemBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {

    private var selectedPosition = -1
    inner class ColorsViewHolder(private val binding : ColorRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(color : Int,position: Int){
            val imageDrawable = ColorDrawable(color)
            binding.imageColors.setImageDrawable(imageDrawable)
            //if color is selected
            if (position==selectedPosition){
                binding.apply {
                    imageColorsIcon.visibility= View.VISIBLE
                    imageShadowColors.visibility = View.VISIBLE
                }
            }else{//if color is not selected
                binding.apply {
                    imageColorsIcon.visibility= View.INVISIBLE
                    imageShadowColors.visibility = View.INVISIBLE
                }
            }
        }

    }

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        return ColorsViewHolder(
            ColorRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color,position)

        holder.itemView.setOnClickListener{
            if (selectedPosition >=0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }

    var onItemClick:((Int)->Unit)?=null
}