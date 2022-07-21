package com.example.firebasechatapp.data.db.remote

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.firebasechatapp.data.models.CreateUser
import com.example.firebasechatapp.data.models.Login
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthSource
@Inject constructor(val auth: FirebaseAuth) {

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

    suspend fun updateUserProfile(name: String, img: String) {
        val profileChanges =
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(img.toUri())
                .build()

        auth.currentUser!!.updateProfile(profileChanges).await()
    }
}