package com.hyskytech.skyshoppy.data

sealed class Category(val category: String) {
    object TShirt : Category("TShirt")
    object Trousers : Category("Trousers")
    object Vests : Category("Vests")
    object Caps : Category("Caps")
    object Accessories : Category("Accessories")
}