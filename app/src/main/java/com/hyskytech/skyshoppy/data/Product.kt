package com.hyskytech.skyshoppy.data

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>,
    val seller : String
){
    constructor() : this("0","","",0,null,null,null,null,images= emptyList(),"")
}
