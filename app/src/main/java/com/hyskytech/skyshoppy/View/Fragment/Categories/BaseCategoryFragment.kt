package com.hyskytech.skyshoppy.View.Fragment.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyskytech.skyshoppy.Adapter.BestProductsAdapter
import com.hyskytech.skyshoppy.Adapter.GreatSavingsAdapter
import com.hyskytech.skyshoppy.Adapter.HotDealsAdapter
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.databinding.FragmentBaseCategoryBinding
import com.hyskytech.skyshoppy.databinding.FragmentMainCategoryBinding
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.BaseCategoryViewModel
import com.hyskytech.skyshoppy.viewModel.MainCategoryViewModel
import kotlinx.coroutines.flow.collectLatest

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding : FragmentBaseCategoryBinding
    protected   val bestProductsRvAdapter : BestProductsAdapter by lazy { BestProductsAdapter() }
    protected  val greatSavingsRvAdapter : GreatSavingsAdapter by lazy { GreatSavingsAdapter() }

    private val viewModel by viewModels<BaseCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPremiumProductsRecyclerView()
        setupGreatSavingsRecyclerView()

    }



    private fun setupGreatSavingsRecyclerView() {

        binding.baseCategoryGreatSavingsRV.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter= greatSavingsRvAdapter
        }
    }

    private fun setupPremiumProductsRecyclerView() {

        binding.baseCategoryBestProductsRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter= bestProductsRvAdapter
        }
    }

    private fun hideLoading() {
        binding.progBaseCategory.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progBaseCategory.visibility = View.VISIBLE
    }

}