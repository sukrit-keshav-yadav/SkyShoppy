package com.hyskytech.skyshoppy.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hyskytech.skyshoppy.data.User
import com.hyskytech.skyshoppy.util.Constants.USER_COLLECTION
import com.hyskytech.skyshoppy.util.RegisterFieldState
import com.hyskytech.skyshoppy.util.RegisterValidation
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.util.validateEmail
import com.hyskytech.skyshoppy.util.validateFirstName
import com.hyskytech.skyshoppy.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db : FirebaseFirestore
) : ViewModel(){

    private val _register = MutableStateFlow<Resource<User>>(Resource.None())
    val register = _register.asStateFlow()

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    fun createAccountWithEmailAndPassword(user: User, password: String){

        if (checkValidUser(user, password)) {

            runBlocking {
                _register.emit(Resource.Loading())
            }

            firebaseAuth.createUserWithEmailAndPassword(user.Email, password)
                .addOnSuccessListener {
                    it.user?.let {userdata ->
                        SaveUserInfo(userdata.uid,user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }else{
            val registerFieldState = RegisterFieldState(
                validateEmail(user.Email),
                validatePassword(password),
                validateFirstName(user.FirstName)
            )

            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun SaveUserInfo(userUid : String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
                Log.d("UserStore", "user data stored successfully")
            }.addOnFailureListener{
                _register.value = Resource.Error(it?.message.toString())
                Log.e("UserStoreError", it?.message.toString() )
            }
    }

    private fun checkValidUser(user: User, password: String) :Boolean {
        val validateEmail = validateEmail(user.Email)
        val validatePassword = validatePassword(password)
        val shouldRegister: Boolean =
            validateEmail is RegisterValidation.Success && validatePassword is
                RegisterValidation.Success
        return shouldRegister
    }
}
