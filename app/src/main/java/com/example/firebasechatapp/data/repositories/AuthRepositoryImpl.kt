package com.example.firebasechatapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.firebasechatapp.data.db.remote.FirebaseAuthSource
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import com.google.firebase.auth.FirebaseUser


class AuthRepositoryImpl constructor(val auth: FirebaseAuthSource, val prefs: SharedPrefsCalls) :
    AuthRepository {

    override fun getUserID(): String {
        return auth.user?.uid!!
    }

    override fun createUser(createUser: CreateUser) {
        auth.createUser(createUser)
    }

    override fun signOut() {
        auth.auth.signOut()
    }

    override fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        auth.loginUser(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
            it.user?.let { user -> prefs.storeUserUid(user.uid) }
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }

    override fun getUser(): LiveData<FirebaseUser?> {
        return auth.getAuthState()
    }
}