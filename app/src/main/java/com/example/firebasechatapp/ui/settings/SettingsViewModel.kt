package com.example.firebasechatapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.data.usecases.SettingsUseCase
import com.example.firebasechatapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val settings: SettingsUseCase
) : ViewModel() {

    val userName = settings.userName()
    val userEmail = settings.userEmail()
    val userImg = settings.userImg()

    private val _isSignOut = MutableLiveData<Event<Boolean>>()
    val isSignOut: LiveData<Event<Boolean>> get() = _isSignOut

    private val _navigateToChangeName = MutableLiveData<Event<Boolean>>()
    val navigateToChangeName: LiveData<Event<Boolean>> get() = _navigateToChangeName

    private val _activateMode = MutableLiveData<Event<Boolean>>()
    val activateMode: LiveData<Event<Boolean>> get() = _activateMode

    fun signOutUser() {
        viewModelScope.launch {
            val job = viewModelScope.launch {
                settings.performLogout()
            }
            job.join()
            if (job.isCompleted)
                _isSignOut.value = Event(true)
        }
    }

    fun navigateToChangeName() {
        _navigateToChangeName.value = Event(true)
    }

    fun toggleMode() {
        if (settings.nightModeState) {
            _activateMode.value = Event(true)
            settings.changeNightMode(false)
        } else {
            _activateMode.value = Event(false)
            settings.changeNightMode(true)
        }
    }
}