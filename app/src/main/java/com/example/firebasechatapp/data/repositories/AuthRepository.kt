package com.example.firebasechatapp.data.repositories

import com.example.firebasechatapp.data.db.remote.FirebaseAuthSource
import com.example.firebasechatapp.utils.Result
import com.google.firebase.auth.FirebaseUser
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import javax.inject.Inject
import kotlin.math.log

class AuthRepository
@Inject constructor(val auth: FirebaseAuthSource){

    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)){
        b.invoke(Result.Loading)
        auth.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }

    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)){
        b.invoke(Result.Loading)
        auth.loginUser(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }
}