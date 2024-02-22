package com.hyskytech.skyshoppy.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.databinding.ActivityShoppingBinding
import dagger.hilt.android.AndroidEntryPoint
import meow.bottomnavigation.MeowBottomNavigation

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.bottomHostFragment)
        binding.apply {
            bottomNavShopping.setupWithNavController(navController)
        }
    }
}