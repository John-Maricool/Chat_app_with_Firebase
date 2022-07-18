package com.example.firebasechatapp.ui.app_components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.SharedPrefsCalls
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    val cloud: CloudRepository,
    val prefs: SharedPrefsCalls
) : ViewModel() {

    private val uid = prefs.getUserUid()

    private val _activateMode = MutableLiveData<Event<Boolean>>()
    val activateMode: LiveData<Event<Boolean>> get() = _activateMode

    fun checkIfNewMessageReceived(): LiveData<Boolean> {
        return if (uid != null) {
            cloud.checkIfUserHasNewMessages(uid)
        } else {
            MutableLiveData(false)
        }
    }

    fun goOnline() {
        if (uid != null) {
            viewModelScope.launch {
                cloud.toggleOnline(uid, true)
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
                cloud.toggleOnline(uid, false)
            }
        }
    }
}