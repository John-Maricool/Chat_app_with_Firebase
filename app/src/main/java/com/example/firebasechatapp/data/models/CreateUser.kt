package com.example.firebasechatapp.data.models

data class CreateUser(
       var displayName: String = "",
       var password: String = "",
       var email: String = ""
    ) {
}