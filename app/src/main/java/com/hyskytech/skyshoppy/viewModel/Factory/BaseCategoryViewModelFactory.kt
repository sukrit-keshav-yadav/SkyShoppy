package com.hyskytech.skyshoppy.viewModel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.Category
import com.hyskytech.skyshoppy.viewModel.BaseCategoryViewModel

class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BaseCategoryViewModel(firestore, category) as T
    }
}