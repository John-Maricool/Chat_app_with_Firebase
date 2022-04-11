package com.example.firebasechatapp.ui.app_components

import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(val auth: FirebaseAuth, val cloud: CloudRepository): ViewModel() {

    fun goOnline(){
        if (auth.currentUser != null) {
            cloud.toggleOnline(auth.currentUser!!.uid, true) {

            }
        }
    }

    fun goOffline(){
        if (auth.currentUser != null) {
            cloud.toggleOnline(auth.currentUser!!.uid, false) {

            }
        }
    }
}