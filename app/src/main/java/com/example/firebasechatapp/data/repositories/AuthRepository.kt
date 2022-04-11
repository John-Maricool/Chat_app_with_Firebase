package com.example.firebasechatapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebasechatapp.data.db.remote.FirebaseAuthSource
import com.example.firebasechatapp.utils.Result
import com.google.firebase.auth.FirebaseUser
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import com.example.firebasechatapp.utils.Event
import javax.inject.Inject
import kotlin.math.log

class AuthRepository
@Inject constructor(val auth: FirebaseAuthSource){

    fun getUserID(): String {
        return auth.user?.uid!!
    }
    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)){
        b.invoke(Result.Loading)
        auth.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }

    fun signOut(){
        auth.auth.signOut()
    }

    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)){
        b.invoke(Result.Loading)
        auth.loginUser(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }

    fun getUser(): LiveData<FirebaseUser?> {
        return auth.getAuthState()
    }
}