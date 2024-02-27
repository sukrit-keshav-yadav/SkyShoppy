package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.Product
import com.hyskytech.skyshoppy.util.Constants
import com.hyskytech.skyshoppy.util.Constants.PRODUCTS_CATEGORY
import com.hyskytech.skyshoppy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private var pagingInfo = PagingInfo()

    private val _hotDeals = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val hotDeals: StateFlow<Resource<List<Product>>> = _hotDeals

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val _greatSavingsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val greatSavingsProducts: StateFlow<Resource<List<Product>>> = _greatSavingsProducts

    init {
        fetchHotDealsProducts()
        fetchBestProducts()
        fetchGreatSavingsProducts()
    }

    fun fetchGreatSavingsProducts() {
        if (!pagingInfo.isPageEnd) {
            viewModelScope.launch {
                _greatSavingsProducts.emit(Resource.Loading())
            }
            firestore.collection("Products").limit(pagingInfo.greatSavingsPage * 10)
                .whereEqualTo(PRODUCTS_CATEGORY, "Great Savings")
                .get()
                .addOnSuccessListener { result ->
                    val greatSavingsProductsList = result.toObjects(Product::class.java)
                    pagingInfo.isPageEnd = greatSavingsProductsList == pagingInfo.oldGreatSavings
                    pagingInfo.oldGreatSavings = greatSavingsProductsList
                    viewModelScope.launch {
                        _greatSavingsProducts.emit(Resource.Success(greatSavingsProductsList))
                    }
                    pagingInfo.greatSavingsPage++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _greatSavingsProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isBPPageEnd) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }

            firestore.collection(Constants.PRODUCTS_COLLECTION)
                .limit(pagingInfo.bestProductsPage * 10)
                .whereEqualTo(PRODUCTS_CATEGORY, "Best Products")
                .get()
                .addOnSuccessListener { result ->
                    val bestProductlist = result.toObjects(Product::class.java)
                    pagingInfo.isBPPageEnd = bestProductlist == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = bestProductlist
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(bestProductlist))
                    }
                    pagingInfo.bestProductsPage++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchHotDealsProducts() {
        if (!pagingInfo.isHDPageEnd) {
            viewModelScope.launch {
                _hotDeals.emit(Resource.Loading())
            }

            firestore.collection(Constants.PRODUCTS_COLLECTION)
                .limit(pagingInfo.hotDealsPage * 10)
                .whereEqualTo(PRODUCTS_CATEGORY, "Hot Deals")
                .get()
                .addOnSuccessListener { result ->
                    val hotDealsProductlist = result.toObjects(Product::class.java)
                    pagingInfo.isHDPageEnd = hotDealsProductlist == pagingInfo.oldHotDeals
                    pagingInfo.oldHotDeals = hotDealsProductlist
                    viewModelScope.launch {
                        _hotDeals.emit(Resource.Success(hotDealsProductlist))
                    }
                    pagingInfo.hotDealsPage++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _hotDeals.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
}

internal data class PagingInfo(
    var greatSavingsPage: Long = 1,
    var bestProductsPage: Long = 1,
    var hotDealsPage: Long = 1,
    var oldGreatSavings: List<Product> = emptyList(),
    var oldHotDeals: List<Product> = emptyList(),
    var oldBestProducts: List<Product> = emptyList(),
    var isPageEnd: Boolean = false,
    var isHDPageEnd: Boolean = false,
    var isBPPageEnd: Boolean = false
)

