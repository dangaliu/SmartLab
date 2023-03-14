package com.example.smartlab.utils

import android.util.Patterns

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}