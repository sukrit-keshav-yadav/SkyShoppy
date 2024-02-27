package com.hyskytech.skyshoppy.View.Fragment.Categories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.Category
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.viewModel.BaseCategoryViewModel
import com.hyskytech.skyshoppy.viewModel.Factory.BaseCategoryViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class VestsFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<BaseCategoryViewModel> { BaseCategoryViewModelFactory(firestore,Category.Vests) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.maxSavingProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showMaxSavingLoading()
                    }
                    is Resource.Success ->{
                        maxSavingsRvAdapter.differ.submitList(it.data)
                        hideMaxSavingLoading()
                    }
                    is Resource.Error ->{
                        hideMaxSavingLoading()
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_LONG).show()
                        Log.e("maxSavingProducts", it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categoryProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showCategoryProductsLoading()
                    }
                    is Resource.Success ->{
                        categoryProductsRvAdapter.differ.submitList(it.data)
                        hideCategoryProductsLoading()
                    }
                    is Resource.Error ->{
                        hideCategoryProductsLoading()
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_LONG).show()
                        Log.e("categoryProducts", it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onMaxSavingPagingRequest() {
        viewModel.fetchMaxSavingsProducts()
    }

    override fun onCategoryProductsPagingRequest() {
        viewModel.fetchCategoryProducts()
    }
}