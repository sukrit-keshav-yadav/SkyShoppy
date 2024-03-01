package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.CartProduct
import com.hyskytech.skyshoppy.firebase.FirebaseCommon
import com.hyskytech.skyshoppy.util.Constants
import com.hyskytech.skyshoppy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartDetailsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.None())
    val addToCart = _addToCart.asStateFlow()

    fun addOrUpdateCartProduct(cartProduct: CartProduct) {

        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
            .collection(Constants.CART_COLLECTION)
            .whereEqualTo("product.id", cartProduct.product.id)
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {//Add New Product
                        addNewProduct(cartProduct)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product!!.product == cartProduct.product && product.selectedColor==cartProduct.selectedColor && product.selectedSize==cartProduct.selectedSize) { //Increase quantity bcoz product already exist in cart
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        } else {//Add new Product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductIntoCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}
