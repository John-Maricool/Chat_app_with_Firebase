package com.example.firebasechatapp.data.db.remote

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import com.example.firebasechatapp.utils.Event
import com.google.firebase.auth.*
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor( val auth: FirebaseAuth) {

    val user = auth.currentUser

    fun createUser(createUser: CreateUser): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(createUser.email, createUser.password)
    }

    fun getAuthState(): MutableLiveData<Event<FirebaseUser?>> {
        val result = MutableLiveData<Event<FirebaseUser?>>()
        auth.addAuthStateListener {
            if (it.currentUser == null && it.uid == null) {
                result.value = Event(it.currentUser)
            } else {
                result.value = Event(null)
            }
        }
        return result
    }

    fun loginUser(login: Login): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(login.email, login.password)
    }
}