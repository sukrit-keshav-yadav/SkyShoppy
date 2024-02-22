package com.hyskytech.skyshoppy.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate : StateFlow<Int> = _navigate

    companion object {
        const val SHOPPING_ACTIVITY = 25
        const val CURRENT_PAGE = 55
    }
    init {
        val user = firebaseAuth.currentUser

        if (user != null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }else{
            viewModelScope.launch {
                _navigate.emit(CURRENT_PAGE)
            }
        }
    }


}