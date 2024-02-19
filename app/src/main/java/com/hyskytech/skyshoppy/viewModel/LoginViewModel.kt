package com.hyskytech.skyshoppy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hyskytech.skyshoppy.util.LoginFieldState
import com.hyskytech.skyshoppy.util.RegisterValidation
import com.hyskytech.skyshoppy.util.Resource
import com.hyskytech.skyshoppy.util.validateEmail
import com.hyskytech.skyshoppy.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel(){

    private val firstName="sky"
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _validation = Channel<LoginFieldState>()
    val validation = _validation.receiveAsFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun login(email: String, password:String){
        if (checkValidUser(email, password)) {

            runBlocking {
                _login.emit(Resource.Loading())
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
        }else{
            val loginFieldState = LoginFieldState(
                validateEmail(email),
                validatePassword(password)
            )

            runBlocking {
                _validation.send(loginFieldState)
            }
        }
    }

    fun resetPassword(email : String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
               viewModelScope.launch {
                   _resetPassword.emit(Resource.Success(email))
               }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun checkValidUser(email: String, password: String): Boolean {
        val validateEmail = validateEmail(email)
        val validatePassword = validatePassword(password)
        return validateEmail is RegisterValidation.Success && validatePassword is
                RegisterValidation.Success
    }
}