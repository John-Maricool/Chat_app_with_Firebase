package com.example.firebasechatapp.data.models

data class CreateUser(
       var displayName: String,
       val password: String,
       val email: String
    ) {
}