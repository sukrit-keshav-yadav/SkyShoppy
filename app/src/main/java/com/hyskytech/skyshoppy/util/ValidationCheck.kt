package com.hyskytech.skyshoppy.util

import android.util.Patterns
import java.util.regex.Pattern

fun validateEmail(email: String):RegisterValidation{
    if(email.isNullOrEmpty())
        return RegisterValidation.Failed("Email Cannot be null")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email Format")

    return RegisterValidation.Success
}

fun validatePassword(password: String):RegisterValidation{
    if(password.isNullOrEmpty())
        return RegisterValidation.Failed("Password Cannot be empty")
    if (password.length < 6)
        return RegisterValidation.Failed("Password must contain 6 characters")
    return RegisterValidation.Success
}