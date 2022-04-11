package com.example.firebasechatapp.utils

fun isEmailValid(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
}

fun isTextValid(minLength: Int, text: String?): Boolean {
    if (text == null || text.isBlank() || text.length < minLength || text.isEmpty()) {
        return false
    }
    return true
}



