package com.hyskytech.skyshoppy.View.Fragment.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyskytech.skyshoppy.Adapter.BestProductsAdapter
import com.hyskytech.skyshoppy.Adapter.GreatSavingsAdapter
import com.hyskytech.skyshoppy.Adapter.HotDealsAdapter
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.databinding.FragmentBaseCategoryBinding
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.util.ShowBottomNav
import com.hyskytech.skyshoppy.viewModel.BaseCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val categoryProductsRvAdapter: GreatSavingsAdapter by lazy { GreatSavingsAdapter() }
    protected val maxSavingsRvAdapter: HotDealsAdapter by lazy { HotDealsAdapter() }


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

        setupCategoryProductsRecyclerView()
        setupMaxSavingsRecyclerView()

        categoryProductsRvAdapter.onClick ={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_fragmentProductInfo,b)
        }
        maxSavingsRvAdapter.onClick ={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_fragmentProductInfo,b)
        }

        binding.baseCategoryMaxSavingProductsRV.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1) && dx != 0) {
                    onMaxSavingPagingRequest()
                }
            }
        })

        binding.nestedScrollviewBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                onCategoryProductsPagingRequest()
            }
        })

    }

    open fun onMaxSavingPagingRequest() {

    }

    open fun onCategoryProductsPagingRequest() {

    }


    private fun setupMaxSavingsRecyclerView() {

        binding.baseCategoryMaxSavingProductsRV.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = maxSavingsRvAdapter
        }
    }

    private fun setupCategoryProductsRecyclerView() {

        binding.baseCategoryProductsRV.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryProductsRvAdapter
        }
    }

    fun hideMaxSavingLoading() {
        binding.progBaseCategoryMaxSavings.visibility = View.GONE
    }

    fun showMaxSavingLoading() {
        binding.progBaseCategoryMaxSavings.visibility = View.VISIBLE
    }

    fun hideCategoryProductsLoading() {
        binding.progBaseCategoryProducts.visibility = View.GONE
    }

    fun showCategoryProductsLoading() {
        binding.progBaseCategoryProducts.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        ShowBottomNav()
    }
}