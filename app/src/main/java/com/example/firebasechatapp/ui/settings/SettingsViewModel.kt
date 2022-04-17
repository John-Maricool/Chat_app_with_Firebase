package com.example.firebasechatapp.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: CloudRepository,
    val auth: AuthRepository,
    val prefs: SharedPreferences
) : ViewModel() {

    private val _result = MutableLiveData<UserInfo?>()
    val result: LiveData<UserInfo?> get() = _result

    private val _isSignOut = MutableLiveData<Event<Boolean>>()
    val isSignOut: LiveData<Event<Boolean>> get() = _isSignOut

    private val _navigateToChangeName = MutableLiveData<Event<Boolean>>()
    val navigateToChangeName: LiveData<Event<Boolean>> get() = _navigateToChangeName

    private val _activateMode = MutableLiveData<Event<Boolean>>()
    val activateMode: LiveData<Event<Boolean>> get() = _activateMode

    init {
        getUserInfo()
    }

    private fun isNightModeActivated(): Boolean {
        return prefs
            .getBoolean(
                "isDarkModeOn", false
            )
    }

    fun signOutUser() {
        auth.signOut()
        _isSignOut.value = Event(true)
    }

    fun navigateToChangeName() {
        _navigateToChangeName.value = Event(true)
    }

    fun goToSavedMedia() {
        TODO()
    }

    fun toggleMode() {
        if (isNightModeActivated()) {
            _activateMode.value = Event(true)
            prefs.edit().putBoolean("isDarkModeOn", false).apply()
        } else {
            _activateMode.value = Event(false)
            prefs.edit().putBoolean("isDarkModeOn", true).apply()
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            cloud.getUserInfo(auth.getUserID()) {
                defaultRepo.onResult(null, it)
                if (it is Result.Success) {
                    _result.value = it.data
                }
            }
        }
    }
}