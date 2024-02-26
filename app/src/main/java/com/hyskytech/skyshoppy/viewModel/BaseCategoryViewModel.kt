package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.Category
import com.hyskytech.skyshoppy.data.Product
import com.hyskytech.skyshoppy.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseCategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category : Category
) : ViewModel() {

    private val _premiumProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val premiumProducts = _premiumProducts.asStateFlow()

    private val _greatSavingsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val greatSavingsProducts = _greatSavingsProducts.asStateFlow()

    init {
        fetchPremiumProducts()
        fetchGreatSavingsProducts()
    }
    fun fetchGreatSavingsProducts(){
        viewModelScope.launch {
            _greatSavingsProducts.emit(Resource.Loading())
        }

        firestore.collection("Products").whereEqualTo("category",category.category).whereGreaterThanOrEqualTo("offerPercentage",50).get()
            .addOnSuccessListener {
                val greatSavingProductsBase = it.toObjects(Product::class.java)
                viewModelScope.launch {
                _greatSavingsProducts.emit(Resource.Success(greatSavingProductsBase))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _greatSavingsProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchPremiumProducts(){
        viewModelScope.launch {
            _premiumProducts.emit(Resource.Loading())
        }

        firestore.collection("Products").whereEqualTo("category",category.category)
            .get()
            .addOnSuccessListener {
                val greatSavingProductsBase = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _premiumProducts.emit(Resource.Success(greatSavingProductsBase))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _premiumProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}