package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.Product
import com.hyskytech.skyshoppy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
):ViewModel() {

    private val _hotDeals = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val hotDeals : StateFlow<Resource<List<Product>>> = _hotDeals

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val bestProducts : StateFlow<Resource<List<Product>>> = _bestProducts

    private val _greatSavingsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val greatSavingsProducts : StateFlow<Resource<List<Product>>> = _greatSavingsProducts
    init {
        fetchHotDealsProducts()
        fetchBestProducts()
        fetchGreatSavingsProducts()
    }

    private fun fetchGreatSavingsProducts() {
        viewModelScope.launch {
            _greatSavingsProducts.emit(Resource.Loading())
        }

        firestore.collection("Products").whereEqualTo("category","Great Savings")
            .get()
            .addOnSuccessListener {result ->
                val greatSavingsProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _greatSavingsProducts.emit(Resource.Success(greatSavingsProductsList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _greatSavingsProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }

        firestore.collection("Products").whereEqualTo("category","Best Products")
            .get()
            .addOnSuccessListener {result ->
                val bestProductlist = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProductlist))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchHotDealsProducts(){

        viewModelScope.launch {
            _hotDeals.emit(Resource.Loading())
        }

        firestore.collection("Products").whereEqualTo("category","Hot Deals")
            .get()
            .addOnSuccessListener {result ->
                val hotDealsProductlist = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _hotDeals.emit(Resource.Success(hotDealsProductlist))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _hotDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}