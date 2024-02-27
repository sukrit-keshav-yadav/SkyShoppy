package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.Category
import com.hyskytech.skyshoppy.data.Product
import com.hyskytech.skyshoppy.util.Constants.PRODUCTS_CATEGORY
import com.hyskytech.skyshoppy.util.Constants.PRODUCTS_COLLECTION
import com.hyskytech.skyshoppy.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseCategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    private var pagingInfoBase = PagingInfoBase()

    private val _maxSavingProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val maxSavingProducts = _maxSavingProducts.asStateFlow()

    private val _categoryProducts = MutableStateFlow<Resource<List<Product>>>(Resource.None())
    val categoryProducts = _categoryProducts.asStateFlow()


    init {
        fetchCategoryProducts()
        fetchMaxSavingsProducts()
    }

    fun fetchMaxSavingsProducts() {
        if (!pagingInfoBase.isMaxSavingPageEnd) {
            viewModelScope.launch {
                _maxSavingProducts.emit(Resource.Loading())
            }
            firestore.collection(PRODUCTS_COLLECTION).whereEqualTo(PRODUCTS_CATEGORY, category.category)
                .whereGreaterThanOrEqualTo("offerPercentage", 50)
                .limit(pagingInfoBase.maxSavingPage * 10)
                .get()
                .addOnSuccessListener {
                    val greatSavingProductsBase = it.toObjects(Product::class.java)
                    pagingInfoBase.isMaxSavingPageEnd =
                        greatSavingProductsBase == pagingInfoBase.oldMaxSaverProducts
                    pagingInfoBase.oldMaxSaverProducts = greatSavingProductsBase
                    viewModelScope.launch {
                        _maxSavingProducts.emit(Resource.Success(greatSavingProductsBase))
                    }
                    pagingInfoBase.maxSavingPage++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _maxSavingProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchCategoryProducts() {
        if (!pagingInfoBase.isPageEnd) {
            viewModelScope.launch {
                _categoryProducts.emit(Resource.Loading())
            }

            firestore.collection(PRODUCTS_COLLECTION).whereEqualTo(PRODUCTS_CATEGORY, category.category)
                .limit(pagingInfoBase.maxSavingPage*10)
                .get()
                .addOnSuccessListener {
                    val categoryProductsBase = it.toObjects(Product::class.java)
                    pagingInfoBase.isPageEnd= categoryProductsBase == pagingInfoBase.oldCategoryProducts
                    pagingInfoBase.oldCategoryProducts = categoryProductsBase
                    viewModelScope.launch {
                        _categoryProducts.emit(Resource.Success(categoryProductsBase))
                    }
                    pagingInfoBase.categoryProductPage++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _categoryProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }
}

internal data class PagingInfoBase(
    var maxSavingPage: Long = 1,
    var categoryProductPage: Long = 1,
    var isPageEnd: Boolean = false,
    var isMaxSavingPageEnd: Boolean = false,
    var oldMaxSaverProducts: List<Product> = emptyList(),
    var oldCategoryProducts: List<Product> = emptyList()
)
