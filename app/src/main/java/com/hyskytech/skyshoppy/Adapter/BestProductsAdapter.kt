package com.hyskytech.skyshoppy.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyskytech.skyshoppy.data.Product
import com.hyskytech.skyshoppy.databinding.BestProductsRvItemBinding
import com.hyskytech.skyshoppy.databinding.HotDealsRvItemBinding

class BestProductsAdapter : RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {
    inner class BestProductsViewHolder(private val binding: BestProductsRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgAd)
                tvAdName.text = product.name
                tvAdPrice.text="₹${product.price.toString()}"
//                tvOldPrice.text="${product.offerPercentage.toString()}% off"
            }
        }
    }



    private val diffUtilCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtilCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            BestProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }
}