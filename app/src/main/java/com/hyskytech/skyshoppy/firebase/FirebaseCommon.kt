package com.hyskytech.skyshoppy.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.CartProduct
import com.hyskytech.skyshoppy.util.Constants

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val cartCollection =
        firestore.collection(Constants.USER_COLLECTION).document(firebaseAuth.uid!!)
            .collection(Constants.CART_COLLECTION)

    fun addProductIntoCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }.addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductobject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRef, newProductobject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }
}