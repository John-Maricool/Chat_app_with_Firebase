package com.example.firebasechatapp.ui.app_components

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.utils.Event
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    val auth: FirebaseAuth,
    val cloud: CloudRepository,
    val prefs: SharedPreferences
) : ViewModel() {

    private val _activateMode = MutableLiveData<Event<Boolean>>()
    val activateMode: LiveData<Event<Boolean>> get() = _activateMode

    fun goOnline() {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                cloud.toggleOnline(auth.currentUser!!.uid, true)
            }
        }
    }

    private fun isNightModeActivated(): Boolean {
        return prefs
            .getBoolean(
                "isDarkModeOn", false
            )
    }

    fun toggleMode() {
        if (isNightModeActivated()) {
            _activateMode.value = Event(true)
        } else {
            _activateMode.value = Event(false)
        }
    }

    fun goOffline() {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                cloud.toggleOnline(auth.currentUser!!.uid, false)
            }
        }
    }
}