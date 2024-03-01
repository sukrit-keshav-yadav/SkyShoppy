package com.hyskytech.skyshoppy.View.Fragment.ProductInfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyskytech.skyshoppy.Adapter.ColorsAdapter
import com.hyskytech.skyshoppy.Adapter.SizesAdapter
import com.hyskytech.skyshoppy.Adapter.ViewPager2ImagesAdapter
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.data.CartProduct
import com.hyskytech.skyshoppy.databinding.FragmentProductInformationBinding
import com.hyskytech.skyshoppy.util.HideBottomNav
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.CartDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProductInfo : Fragment(R.layout.fragment_product_information) {

    private val args by navArgs<FragmentProductInfoArgs>()

    private lateinit var binding: FragmentProductInformationBinding
    private val viewPagerAdapter by lazy { ViewPager2ImagesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val viewModel by viewModels<CartDetailsViewModel>()
    private var selectedColor: Int? = null
    private var selectedSize: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        HideBottomNav()
        binding = FragmentProductInformationBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRV()
        setupColorsRV()
        setupViewPager()

        sizesAdapter.onItemClick = {
            selectedSize = it
        }
        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.addOrUpdateCartProduct(CartProduct(product, 1, selectedColor, selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnAddToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.btnAddToCart.revertAnimation()
                        binding.btnAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                        Toast.makeText(
                            requireContext(),
                            "Product Added to Cart Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        binding.btnAddToCart.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        binding.apply {
            TVPNameProductInfo.text = product.name
            TVDescriptionPI.text = product.description
            product.offerPercentage?.let {
                val remainingPricePercentage = 100 - it
                val priceAfterOffer: Int =
                    ((remainingPricePercentage * product.price) / 100).toInt()
                binding.TVpriceProductInfo.text = "₹ $priceAfterOffer"
            }
            TVSellerPI.text = "Sold By: ${product.seller}"

            if (product.colors.isNullOrEmpty()) {
                TVColorsPI.visibility = View.GONE
                RVColorsPI.visibility = View.GONE
            }

            if (product.sizes.isNullOrEmpty()) {
                TVSizePI.visibility = View.GONE
                RVSizePI.visibility = View.GONE
            }
        }

        binding.closeIV.setOnClickListener {
            findNavController().navigateUp()
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.sizes?.let {
            sizesAdapter.differ.submitList(product.sizes)
        }
        product.colors?.let {
            colorsAdapter.differ.submitList(product.colors)
        }
    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductsInfo.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRV() {
        binding.RVColorsPI.apply {
            adapter = colorsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRV() {
        binding.RVSizePI.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}