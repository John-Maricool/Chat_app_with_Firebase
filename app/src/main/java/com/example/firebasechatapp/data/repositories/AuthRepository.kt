package com.example.firebasechatapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import com.example.firebasechatapp.utils.Result
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    fun getUserID(): String
    fun createUser(createUser: CreateUser)
    fun signOut()
    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit))
    fun getUser(): LiveData<FirebaseUser?>
}