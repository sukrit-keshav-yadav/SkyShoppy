package com.hyskytech.skyshoppy.data

data class User(
    val Name : String,
    val Email : String,
    val imagePath : String = ""
)
{
    constructor(): this("","","")
}

