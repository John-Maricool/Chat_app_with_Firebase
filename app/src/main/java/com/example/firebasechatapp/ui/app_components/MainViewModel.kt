package com.example.firebasechatapp.ui.app_components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.abstractions.CloudRepository
import com.example.firebasechatapp.data.repositories.abstractions.RemoteUserRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.SharedPrefsCalls
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    val cloud: RemoteUserRepository,
    val prefs: SharedPrefsCalls
) : ViewModel() {

    private val uid = prefs.getUserUid()

    private val _activateMode = MutableLiveData<Event<Boolean>>()
    val activateMode: LiveData<Event<Boolean>> get() = _activateMode

    var isNewMessage: LiveData<Boolean> = MutableLiveData()


    fun checkIfNewMessageReceived() {
        if (uid != null) {
            viewModelScope.launch {
                isNewMessage = cloud.checkIfUserHasNewMessages(uid)
            }
        } else {
            MutableLiveData(false)
        }
    }

    fun goOnline() {
        if (uid != null) {
            viewModelScope.launch {
                cloud.toggleOnline(true)
            }
        }
    }

    fun toggleMode() {
        if (prefs.isNightModeOn()) {
            _activateMode.value = Event(true)
        } else {
            _activateMode.value = Event(false)
        }
    }

    fun goOffline() {
        if (uid != null) {
            viewModelScope.launch {
                cloud.toggleOnline(false)
            }
        }
    }
}