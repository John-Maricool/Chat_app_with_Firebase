package com.example.firebasechatapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatapp.cache_source.UserDao
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.CloudRepositoryImpl
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import com.example.firebasechatapp.utils.SharedPrefsCalls
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: CloudRepository,
    val auth: AuthRepository,
    val prefs: SharedPrefsCalls,
    val dao: UserDao
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

    fun signOutUser() {
        viewModelScope.launch {
            val job = viewModelScope.launch {
                auth.signOut()
                prefs.resetUserUid()
                dao.deleteAll()
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
        if (prefs.isNightModeOn()) {
            _activateMode.value = Event(true)
            prefs.changeNightMode(false)
        } else {
            _activateMode.value = Event(false)
            prefs.changeNightMode(true)
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            prefs.getUserUid()?.let { uid ->
                cloud.getUserInfo(uid) {
                    defaultRepo.onResult(null, it)
                    if (it is Result.Success) {
                        _result.value = it.data
                    }
                }
            }
        }
    }
}