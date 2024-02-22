package com.hyskytech.skyshoppy.Adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyskytech.skyshoppy.data.Product
import com.hyskytech.skyshoppy.databinding.BestProductsRvItemBinding
import com.hyskytech.skyshoppy.databinding.GreatSavingsRvItemBinding

class GreatSavingsAdapter : RecyclerView.Adapter<GreatSavingsAdapter.GreatSavingsViewHolder>() {
    inner class GreatSavingsViewHolder(private val binding: GreatSavingsRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                tvName.text = product.name
                tvPrice.text="₹${product.price.toString()}"
                product.offerPercentage?.let {
                    val remainingPricePercentage = 100 - it
                    val priceAfterOffer : Int = ((remainingPricePercentage * product.price)/100).toInt()
                    tvNewPrice.text= "₹${priceAfterOffer.toString()}"
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if(product.offerPercentage==null) tvNewPrice.visibility = View.INVISIBLE
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GreatSavingsViewHolder {
        return GreatSavingsViewHolder(
            GreatSavingsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: GreatSavingsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }
}