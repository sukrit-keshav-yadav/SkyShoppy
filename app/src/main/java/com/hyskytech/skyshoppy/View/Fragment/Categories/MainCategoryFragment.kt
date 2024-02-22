package com.hyskytech.skyshoppy.View.Fragment.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var hotDealsRvAdapter : HotDealsAdapter
    private lateinit var bestProductsRvAdapter : BestProductsAdapter
    private lateinit var greatSavingsRvAdapter : GreatSavingsAdapter

    private val viewmodel by viewModels<MainCategoryViewModel>()

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
            viewmodel.hotDeals.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("HotDeals", it.message.toString() )
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        hotDealsRvAdapter.differ.submitList(it.data)
                        binding.CVHotDealsHome.visibility = View.VISIBLE
                        hideLoading()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("BestProducts", it.message.toString() )
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        bestProductsRvAdapter.differ.submitList(it.data)
                        binding.CVBestProductsHome.visibility = View.VISIBLE
                        hideLoading()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.greatSavingsProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("greatSavingsProducts", it.message.toString() )
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        greatSavingsRvAdapter.differ.submitList(it.data)
                        binding.CVGreatSavingsHome.visibility = View.VISIBLE
                        hideLoading()
                    }

                    else -> {}
                }
            }
        }

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
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter= hotDealsRvAdapter
        }
    }
}