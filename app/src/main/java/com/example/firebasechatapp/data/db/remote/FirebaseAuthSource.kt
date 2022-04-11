package com.example.firebasechatapp.data.db.remote

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import com.example.firebasechatapp.utils.Event
import com.google.firebase.auth.*
import javax.inject.Inject

class FirebaseAuthSource
@Inject constructor( val auth: FirebaseAuth) {

    val user = auth.currentUser

    fun createUser(createUser: CreateUser): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(createUser.email, createUser.password)
    }

    fun getAuthState(): MutableLiveData<FirebaseUser?> {
        val result = MutableLiveData<FirebaseUser?>()
        auth.addAuthStateListener {
            if (it.currentUser != null && it.uid != null) {
                result.value = it.currentUser
            } else {
                result.value = null
            }
        }
        return result
    }

    fun loginUser(login: Login): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(login.email, login.password)
    }
}