package com.hyskytech.skyshoppy.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hyskytech.skyshoppy.R
import com.hyskytech.skyshoppy.View.Activity.ShoppingActivity

fun Fragment.HideBottomNav(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavShopping)
    bottomNavigationView.visibility = View.GONE
}
fun Fragment.ShowBottomNav(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavShopping)
    bottomNavigationView.visibility = View.VISIBLE
}