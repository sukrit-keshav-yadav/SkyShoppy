package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hyskytech.skyshoppy.data.User
import com.hyskytech.skyshoppy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel(){

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.None())
    val register : Flow<Resource<FirebaseUser>> = _register
    fun createAccountWithEmailAndPassword(user: User, password: String){
        firebaseAuth.createUserWithEmailAndPassword(user.Email,password)
            .addOnSuccessListener {
                it.user?.let {
                    _register.value=Resource.Success(it)
                }
            }.addOnFailureListener{
                _register.value=Resource.Error(it.message.toString())
            }
    }
}
