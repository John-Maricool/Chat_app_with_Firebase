package com.example.firebasechatapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.data.repositories.AuthRepository
import com.example.firebasechatapp.data.repositories.CloudRepository
import com.example.firebasechatapp.data.repositories.DefaultRepository
import com.example.firebasechatapp.utils.Event
import com.example.firebasechatapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    val defaultRepo: DefaultRepository,
    val cloud: CloudRepository,
    val auth: AuthRepository
) : ViewModel() {

    private val _result = MutableLiveData<UserInfo?>()
    val result: LiveData<UserInfo?> get() = _result

    private val _isSignOut = MutableLiveData<Event<Unit>>()
    val isSignOut: LiveData<Event<Unit>> get() = _isSignOut

    private val _navigateToChangeName = MutableLiveData<Event<Unit>>()
    val navigateToChangeName: LiveData<Event<Unit>> get() = _navigateToChangeName

    private val _navigateToChangeImage = MutableLiveData<Event<Unit>>()
    val navigateToChangeImage: LiveData<Event<Unit>> get() = _navigateToChangeImage

    init {
        getUserInfo()
    }

    fun signOutUser(){
        auth.signOut()
        _isSignOut.value = Event(Unit)
    }

    fun navigateToChangeName(){
        _navigateToChangeName.value = Event(Unit)
    }

    fun navigateToChangeImage(){
        _navigateToChangeName.value = Event(Unit)
    }

    fun shareApp(){
        TODO()
    }

    fun toggleMode(){
        TODO()
    }

    private fun getUserInfo(){
        cloud.getUserInfo(auth.getUserID()){
            defaultRepo.onResult(null, it)
            if (it is Result.Success){
                _result.value = it.data
            }
        }
    }
}