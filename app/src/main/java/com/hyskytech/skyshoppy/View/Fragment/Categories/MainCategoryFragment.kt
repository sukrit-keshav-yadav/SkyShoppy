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
import com.hyskytech.skyshoppy.databinding.FragmentMainCategoryBinding
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var hotDealsRvAdapter : HotDealsAdapter
    private lateinit var bestProductsRvAdapter : BestProductsAdapter
    private lateinit var greatSavingsRvAdapter : GreatSavingsAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHotDealsRecyclerView()
        setupBestProductsRecyclerView()
        setupGreatSavingsRecyclerView()

        lifecycleScope.launchWhenStarted {
            viewModel.hotDeals.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progBarHotDeals.visibility=View.VISIBLE
                        showLoading()
                    }
                    is Resource.Error -> {
                        binding.progBarHotDeals.visibility=View.GONE
                        hideLoading()
                        Log.e("HotDeals", it.message.toString() )
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        hotDealsRvAdapter.differ.submitList(it.data)
                        binding.progBarHotDeals.visibility=View.GONE
                        hideLoading()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progBarBestProducts.visibility=View.VISIBLE
                        showLoading()
                    }
                    is Resource.Error -> {
                        binding.progBarBestProducts.visibility=View.GONE
                        hideLoading()
                        Log.e("BestProducts", it.message.toString() )
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        bestProductsRvAdapter.differ.submitList(it.data)
                        binding.progBarBestProducts.visibility=View.GONE
                        hideLoading()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.greatSavingsProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progBarGreatSavings.visibility=View.VISIBLE
                        showLoading()
                    }
                    is Resource.Error -> {
                        binding.progBarGreatSavings.visibility=View.GONE
                        hideLoading()
                        Log.e("greatSavingsProducts", it.message.toString() )
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        greatSavingsRvAdapter.differ.submitList(it.data)
                        binding.progBarGreatSavings.visibility=View.GONE
                        hideLoading()
                    }

                    else -> {}
                }
            }
        }

        binding.NestedScrollMainCat.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v, scrollX, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchGreatSavingsProducts()
            }
            if (v.getChildAt(0).right <= (scrollX - v.width)){
                viewModel.fetchHotDealsProducts()
                viewModel.fetchBestProducts()
            }
        })
    }



    private fun setupGreatSavingsRecyclerView() {
        greatSavingsRvAdapter = GreatSavingsAdapter()

        binding.RVGreatSavingsHome.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter= greatSavingsRvAdapter
        }
    }

    private fun setupBestProductsRecyclerView() {
        bestProductsRvAdapter = BestProductsAdapter()

        binding.RVBestProductsHome.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter= bestProductsRvAdapter
        }
    }

    private fun hideLoading() {
        binding.progBarHome.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progBarHome.visibility = View.VISIBLE
    }

    private fun setupHotDealsRecyclerView() {
        hotDealsRvAdapter = HotDealsAdapter()

        binding.RVHotDealsHome.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter= hotDealsRvAdapter
        }
    }
}