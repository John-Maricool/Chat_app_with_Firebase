package com.example.firebasechatapp.data.db.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor( val auth: FirebaseAuth) {

    fun createUser(createUser: CreateUser): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(createUser.email, createUser.password)
    }
    fun loginUser(login: Login): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(login.email, login.password)
    }
}