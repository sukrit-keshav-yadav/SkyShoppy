package com.hyskytech.skyshoppy.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyskytech.skyshoppy.databinding.ViewpagerImageItemBinding

class ViewPager2ImagesAdapter : RecyclerView.Adapter<ViewPager2ImagesAdapter.ImagesViewHolder>() {

    inner class ImagesViewHolder(private val binding : ViewpagerImageItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(imagePath : String){
            Glide.with(itemView).load(imagePath).into(binding.IVImagesViewPager)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

}